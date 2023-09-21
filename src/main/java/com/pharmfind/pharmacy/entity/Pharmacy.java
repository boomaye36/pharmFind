package com.pharmfind.pharmacy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "pharmacy")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Pharmacy {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pharmacyName;
    private String pharmacyAddress;
    private double latitude;
    private double longitude;
}
