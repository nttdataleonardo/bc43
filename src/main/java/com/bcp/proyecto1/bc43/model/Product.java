package com.bcp.proyecto1.bc43.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private ProductType type;
    private String idClient;
    private BigDecimal balance;
    private BigDecimal credit;

    @JsonIgnore
    private BigDecimal paycredit;


    public Product(String name, ProductType type, String idClient, BigDecimal balance, BigDecimal credit) {
        this.name = name;
        this.type = type;
        this.idClient = idClient;
        this.balance = balance;
        this.credit = credit;
    }

    public Product(){
        this.balance = BigDecimal.ZERO;
        this.paycredit = BigDecimal.ZERO;
    }

}
