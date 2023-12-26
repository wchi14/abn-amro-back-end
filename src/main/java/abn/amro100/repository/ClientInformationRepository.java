package abn.amro100.repository;

import abn.amro100.entity.ClientInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientInformationRepository extends JpaRepository<ClientInformation, Long> {

    Optional<ClientInformation> findByClientTypeAndClientNumberAndAccountNumberAndSubAccountNumber(
            String clientType,
            String clientNumber,
            String accountNumber,
            String subAccountNumber
    );
}