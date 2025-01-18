package com.ogan.FinOm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // KYC details
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String stateOfOrigin;
    private String email;
    private String phoneNumber;

    // Account Details;
    @Column(unique = true, nullable = false)
    private String accountNumber;
    private BigDecimal accountBalance;
    private String status;

    // Audit details
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

}
