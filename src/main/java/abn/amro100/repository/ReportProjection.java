package abn.amro100.repository;

public interface ReportProjection {

    String getClientType();

    String getClientNumber();

    String getAccountNumber();

    String getSubAccountNumber();

    String getExchangeCode();

    String getProductGroupCode();

    String getSymbol();

    String getExpirationDate();

    Long getTotalTransactionAmount();
}
