package com.example.bankcards.entity;

import com.example.bankcards.util.CardSerialNumberUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String serialNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private CardStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonGetter("serialNumber")
    public String getEncodedSerialNumber() {
        return CardSerialNumberUtil.encodeSerialNumber(this.serialNumber);
    }
}
