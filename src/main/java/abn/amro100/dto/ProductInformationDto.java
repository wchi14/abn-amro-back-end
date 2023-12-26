package abn.amro100.dto;

public record ProductInformationDto(
        String exchangeCode,
        String productGroupCode,
        String symbol,
        String expirationDate) {
}
