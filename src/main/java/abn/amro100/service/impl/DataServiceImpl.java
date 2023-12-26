package abn.amro100.service.impl;

import abn.amro100.constant.Constant;
import abn.amro100.dto.ClientInformationDto;
import abn.amro100.dto.ProductInformationDto;
import abn.amro100.dto.RecordDto;
import abn.amro100.entity.ClientInformation;
import abn.amro100.entity.ProductInformation;
import abn.amro100.entity.Transaction;
import abn.amro100.exception.InvalidSourceFileException;
import abn.amro100.repository.ClientInformationRepository;
import abn.amro100.repository.ProductInformationRepository;
import abn.amro100.repository.TransactionRepository;
import abn.amro100.service.DataService;
import abn.amro100.transformer.DataTransformer;
import abn.amro100.util.FileUtils;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataServiceImpl implements DataService {

    private final TransactionRepository transactionRepository;
    private final ClientInformationRepository clientInformationRepository;
    private final ProductInformationRepository productInformationRepository;

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void inputData() throws IOException, InvalidSourceFileException {
        List<String> strings = FileUtils.readFileFromResource("/templates/input.txt");

        if (strings.stream().anyMatch(str -> str.length() > Constant.MAX_LENGTH_OF_RECORD)) {
            log.error("The source file have a record exceed 303 characters");
            throw new InvalidSourceFileException();
        }

        DataTransformer.transformToTransactionList(strings).forEach(transaction -> {
            // map to Entity for saving to database

            ClientInformation clientInformation = getClientInformation(transaction.clientInformationDto());

            ProductInformation productInformation = getProductInformation(transaction.productInformationDto());

            Transaction transactionEntity = new Transaction();
            transactionEntity.setClientInformation(clientInformation);
            transactionEntity.setProductInformation(productInformation);
            transactionEntity.setTransactionAmount(transaction.transactionAmount());
            transactionRepository.save(transactionEntity);
        });
    }

    private ProductInformation getProductInformation(ProductInformationDto productInformationDto) {
        Optional<ProductInformation> productInformationOptional = productInformationRepository.findByExchangeCodeAndProductGroupCodeAndSymbolAndExpirationDate(
                productInformationDto.exchangeCode(),
                productInformationDto.productGroupCode(),
                productInformationDto.symbol(),
                productInformationDto.expirationDate()
        );

        // Check if database has the product information
        if (productInformationOptional.isEmpty()) {
            ProductInformation productInformation = new ProductInformation();
            productInformation.setExchangeCode(productInformationDto.exchangeCode());
            productInformation.setProductGroupCode(productInformationDto.productGroupCode());
            productInformation.setSymbol(productInformationDto.symbol());
            productInformation.setExpirationDate(productInformationDto.expirationDate());
            return productInformationRepository.saveAndFlush(productInformation);
        } else {
            return productInformationOptional.get();
        }
    }

    private ClientInformation getClientInformation(ClientInformationDto clientInformationDto) {
        Optional<ClientInformation> clientInformationOptional = clientInformationRepository.findByClientTypeAndClientNumberAndAccountNumberAndSubAccountNumber(
                clientInformationDto.clientType(),
                clientInformationDto.clientNumber(),
                clientInformationDto.accountNumber(),
                clientInformationDto.subAccountNumber()
        );

        // Check if database has the product information
        if (clientInformationOptional.isEmpty()) {
            ClientInformation clientInformation = new ClientInformation();
            clientInformation.setClientType(clientInformationDto.clientType());
            clientInformation.setClientNumber(clientInformationDto.clientNumber());
            clientInformation.setAccountNumber(clientInformationDto.accountNumber());
            clientInformation.setSubAccountNumber(clientInformationDto.subAccountNumber());
            return clientInformationRepository.saveAndFlush(clientInformation);
        } else {
            return clientInformationOptional.get();
        }
    }

    @Override
    public List<RecordDto> getData() {
        return getRecordDtos();
    }

    @Override
    public StreamingResponseBody getCsvData() {
        List<RecordDto> recordDtos = getRecordDtos();

        HeaderColumnNameMappingStrategy<RecordDto> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(RecordDto.class);

        String headerLine = Arrays.stream(RecordDto.class.getDeclaredFields())
                .map(field -> field.getAnnotation(CsvBindByName.class))
                .filter(Objects::nonNull)
                .map(CsvBindByName::column)
                .collect(Collectors.joining(","));

        // Initialize strategy
        try (StringReader reader = new StringReader(headerLine)) {
            CsvToBean<RecordDto> csv = new CsvToBeanBuilder<RecordDto>(reader)
                    .withType(RecordDto.class)
                    .withIgnoreQuotations(true)
                    .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(strategy)
                    .build();
            for (RecordDto recordDto : csv) {}
        }
        return outputStream -> {
            try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                try {
                    new StatefulBeanToCsvBuilder<RecordDto>(writer)
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .withMappingStrategy(strategy)
                            .build().write(recordDtos);
                } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        };
    }

    private List<RecordDto> getRecordDtos() {
        return transactionRepository
                .getTotalTransactionAmountWithClientAndProductInformation()
                .stream()
                .map(DataTransformer::transformToRecordDto)
                .collect(Collectors.toList());
    }

}
