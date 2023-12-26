package abn.amro100.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "product_information")
public class ProductInformation {

    public ProductInformation() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exchangeCode;

    private String productGroupCode;

    private String symbol;

    private String expirationDate;
}