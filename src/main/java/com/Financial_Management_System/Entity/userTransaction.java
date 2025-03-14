package com.Financial_Management_System.Entity;

import java.time.LocalDateTime;

import com.Financial_Management_System.DTO.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class userTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionid;

    private Double amount;

    @ManyToOne
    @JoinColumn
    private UserEntity user;

    private String username;

    @Enumerated(value = EnumType.STRING)
    private TransactionType type;

    private LocalDateTime dateTime;

    private String category;

    private String description;
}
