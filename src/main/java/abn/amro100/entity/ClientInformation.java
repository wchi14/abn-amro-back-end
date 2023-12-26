package abn.amro100.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity(name = "client_information")
public class ClientInformation {

    public ClientInformation() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientType;

    private String clientNumber;

    private String accountNumber;

    private String subAccountNumber;
}
