package abn.amro100.transformer;

import abn.amro100.dto.ClientInformationDto;
import abn.amro100.dto.ProductInformationDto;
import abn.amro100.dto.RecordDto;
import abn.amro100.dto.TransactionDto;
import abn.amro100.repository.ReportProjection;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DataTransformer {

    public static List<TransactionDto> transformToTransactionList(List<String> strings) {
        return strings.stream()
                .map(str -> new TransactionDto(
                        new ClientInformationDto(
                                str.substring(3, 7).trim(),
                                str.substring(7, 11).trim(),
                                str.substring(11, 15).trim(),
                                str.substring(15, 19).trim()
                        ),
                        new ProductInformationDto(
                                str.substring(27, 31).trim(),
                                str.substring(25, 27).trim(),
                                str.substring(31, 37).trim(),
                                str.substring(37, 45).trim()
                        ),
                        ((str.charAt(51) == ' ')? 1: -1) * Long.parseLong(str.substring(52, 62)) - ((str.charAt(62) == ' ')? 1: -1) * Long.parseLong(str.substring(63, 73))
                ))
                .collect(Collectors.toList());
    }

    public static RecordDto transformToRecordDto(ReportProjection reportProjection) {
        return new RecordDto(
                reportProjection.getClientType(),
                reportProjection.getClientNumber(),
                reportProjection.getAccountNumber(),
                reportProjection.getSubAccountNumber(),
                reportProjection.getExchangeCode(),
                reportProjection.getProductGroupCode(),
                reportProjection.getSymbol(),
                reportProjection.getExpirationDate(),
                reportProjection.getTotalTransactionAmount()
        );
    }
}
