package com.zoozo.zoozoforsellers.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
public class Product {

    @Id
    private String productCode;

    private String productName;

    @Lob
    private String description;

    private float sellingPrice;

    private String inStock;

    private float productValue;

    private float commission;
}
