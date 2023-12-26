package abn.amro100.dto;

public record TransactionDto(ClientInformationDto clientInformationDto,
                             ProductInformationDto productInformationDto,
                             Long transactionAmount) {
}
