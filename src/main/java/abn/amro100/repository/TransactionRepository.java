package abn.amro100.repository;

import abn.amro100.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT s.clientInformation.clientType as clientType, " +
            "s.clientInformation.clientNumber as clientNumber, " +
            "s.clientInformation.accountNumber as accountNumber, " +
            "s.clientInformation.subAccountNumber as subAccountNumber, " +
            "s.productInformation.exchangeCode as exchangeCode, " +
            "s.productInformation.productGroupCode as productGroupCode, " +
            "s.productInformation.symbol as symbol, " +
            "s.productInformation.expirationDate as expirationDate, " +
            "SUM(s.transactionAmount) as totalTransactionAmount from transaction s GROUP BY s.clientInformation.id, s.productInformation.id")
    List<ReportProjection> getTotalTransactionAmountWithClientAndProductInformation();
}