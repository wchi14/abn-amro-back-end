package abn.amro100.repository;

import abn.amro100.entity.ProductInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInformationRepository extends JpaRepository<ProductInformation, Long> {

    Optional<ProductInformation> findByExchangeCodeAndProductGroupCodeAndSymbolAndExpirationDate(
            String exchangeCode,
            String productGroupCode,
            String symbol,
            String expirationDate
    );

}