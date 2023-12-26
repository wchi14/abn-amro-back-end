package abn.amro100.entity;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity(name = "transaction")
public class Transaction {

    public Transaction() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_information_id", referencedColumnName = "id")
    private ClientInformation clientInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_information_id", referencedColumnName = "id")
    private ProductInformation productInformation;

    private Long transactionAmount;
}