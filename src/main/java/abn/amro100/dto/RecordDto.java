package abn.amro100.dto;

import com.opencsv.bean.CsvBindByName;

public record RecordDto(@CsvBindByName(column = "Client Type") String clientType,
                        @CsvBindByName(column = "Client Number") String clientNumber,
                        @CsvBindByName(column = "Account Number") String accountNumber,
                        @CsvBindByName(column = "Sub Account Number") String subAccountNumber,
                        @CsvBindByName(column = "Exchange Code") String exchangeCode,
                        @CsvBindByName(column = "Product Group Code") String productGroupCode,
                        @CsvBindByName(column = "Symbol") String symbol,
                        @CsvBindByName(column = "Expiration Date") String expirationDate,
                        @CsvBindByName(column = "Total Transaction Amount") Long totalTransactionAmount) {

}
