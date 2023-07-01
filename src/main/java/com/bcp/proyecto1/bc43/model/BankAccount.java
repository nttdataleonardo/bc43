package com.bcp.proyecto1.bc43.model;

import com.bcp.proyecto1.bc43.exceptions.NotFoundException;
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
@Document(collection = "bankaccounts")
public class BankAccount {
    @Id
    private String id;
    private BigDecimal balance;

    private BigDecimal productId;

    public BankAccount(){
        this.balance = BigDecimal.ZERO;
    }

    public void makeDeposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void makeWithdrawal(BigDecimal amount) {
        if (amount.compareTo(balance) <= 0) {
            balance = balance.subtract(amount);
        } else {
            throw new NotFoundException("Saldo insuficiente en la cuenta bancaria.");
        }
    }
}
