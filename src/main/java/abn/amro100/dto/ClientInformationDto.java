package abn.amro100.dto;

public record ClientInformationDto(
        String clientType,
        String clientNumber,
        String accountNumber,
        String subAccountNumber) {
}
