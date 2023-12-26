package abn.amro100.transformer;

import abn.amro100.dto.RecordDto;
import abn.amro100.dto.TransactionDto;
import abn.amro100.repository.ReportProjection;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataTransformerTest {

    @Test
    public void testTransformToTransactionList() {
        List<String> strings = Arrays.asList(
                "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012380     688032000092500000000             O",
                "315CL  432100030001FCC   FUCME N1    20100910JPY01S 0000000000 0000000003000000000000DUSD000000000015DUSD000000000000DJPY20100819059611      000444000092100000000             O"
        );

        List<TransactionDto> result = DataTransformer.transformToTransactionList(strings);

        assertEquals(2, result.size());

        TransactionDto transactionDto1 = result.get(0);
        assertEquals("CL", transactionDto1.clientInformationDto().clientType());
        assertEquals("4321", transactionDto1.clientInformationDto().clientNumber());
        assertEquals("0002", transactionDto1.clientInformationDto().accountNumber());
        assertEquals("0001", transactionDto1.clientInformationDto().subAccountNumber());
        assertEquals("SGX", transactionDto1.productInformationDto().exchangeCode());
        assertEquals("FU", transactionDto1.productInformationDto().productGroupCode());
        assertEquals("NK", transactionDto1.productInformationDto().symbol());
        assertEquals("20100910", transactionDto1.productInformationDto().expirationDate());
        assertEquals(1, transactionDto1.transactionAmount());

        TransactionDto transactionDto2 = result.get(1);
        assertEquals("CL", transactionDto2.clientInformationDto().clientType());
        assertEquals("4321", transactionDto2.clientInformationDto().clientNumber());
        assertEquals("0003", transactionDto2.clientInformationDto().accountNumber());
        assertEquals("0001", transactionDto2.clientInformationDto().subAccountNumber());
        assertEquals("CME", transactionDto2.productInformationDto().exchangeCode());
        assertEquals("FU", transactionDto2.productInformationDto().productGroupCode());
        assertEquals("N1", transactionDto2.productInformationDto().symbol());
        assertEquals("20100910", transactionDto2.productInformationDto().expirationDate());
        assertEquals(-3, transactionDto2.transactionAmount());
    }

    @Test
    public void testTransformToRecordDto() {
        ReportProjection reportProjection = new ReportProjection() {
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

        RecordDto result = DataTransformer.transformToRecordDto(reportProjection);

        assertEquals("CL", result.clientType());
        assertEquals("4321", result.clientNumber());
        assertEquals("0003", result.accountNumber());
        assertEquals("0001", result.subAccountNumber());
        assertEquals("CME", result.exchangeCode());
        assertEquals("FU", result.productGroupCode());
        assertEquals("N1", result.symbol());
        assertEquals("20100910", result.expirationDate());
        assertEquals(-3L, result.totalTransactionAmount());
    }
}
