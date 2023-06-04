package com.zoozo.zoozoforsellers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String productCode;

    private float sellingPrice;

    private String status;

    private int quantity;

    private String sellerId;

    @Lob
    private String customerDetails;
}
