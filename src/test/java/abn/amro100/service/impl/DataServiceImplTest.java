package abn.amro100.service.impl;

import abn.amro100.entity.Transaction;
import abn.amro100.exception.InvalidSourceFileException;
import abn.amro100.repository.ClientInformationRepository;
import abn.amro100.repository.ProductInformationRepository;
import abn.amro100.repository.ReportProjection;
import abn.amro100.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DataServiceImplTest {

    private DataServiceImpl dataService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ClientInformationRepository clientInformationRepository;

    @Mock
    private ProductInformationRepository productInformationRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        dataService = new DataServiceImpl(transactionRepository, clientInformationRepository, productInformationRepository);
    }

    @Test
    public void testInputData() throws IOException, InvalidSourceFileException {

        when(productInformationRepository.findByExchangeCodeAndProductGroupCodeAndSymbolAndExpirationDate(
                any(), any(), any(), any())).thenReturn(Optional.empty());

        when(clientInformationRepository.findByClientTypeAndClientNumberAndAccountNumberAndSubAccountNumber(
                any(), any(), any(), any())).thenReturn(Optional.empty());

        dataService.inputData();

        verify(transactionRepository, times(717)).save(any(Transaction.class));
    }

    @Test
    public void testGetCsvData() throws IOException {
        List<ReportProjection> reportProjections = new ArrayList<>();
        reportProjections.add(getDummyReportProjection());
        reportProjections.add(getDummyReportProjection());

        when(transactionRepository.getTotalTransactionAmountWithClientAndProductInformation()).thenReturn(reportProjections);

        StreamingResponseBody responseBody = dataService.getCsvData();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        responseBody.writeTo(outputStream);

        String actualCsvData = outputStream.toString(StandardCharsets.UTF_8);

        String expectedCsvData = """
                Client Type,Client Number,Account Number,Sub Account Number,Exchange Code,Product Group Code,Symbol,Expiration Date,Total Transaction Amount
                CL,4321,0003,0001,CME,FU,N1,20100910,-3
                CL,4321,0003,0001,CME,FU,N1,20100910,-3 
                """;

        assertEquals(expectedCsvData, actualCsvData);

    }

    private static ReportProjection getDummyReportProjection() {
        return new ReportProjection() {
            @Override
            public String getClientType() {
                return "CL";
            }

            @Override
            public String getClientNumber() {
                return "4321";
            }

            @Override
            public String getAccountNumber() {
                return "0003";
            }

            @Override
            public String getSubAccountNumber() {
                return "0001";
            }

            @Override
            public String getExchangeCode() {
                return "CME";
            }

            @Override
            public String getProductGroupCode() {
                return "FU";
            }

            @Override
            public String getSymbol() {
                return "N1";
            }

            @Override
            public String getExpirationDate() {
                return "20100910";
            }

            @Override
            public Long getTotalTransactionAmount() {
                return -3L;
            }
        };
    }
}
