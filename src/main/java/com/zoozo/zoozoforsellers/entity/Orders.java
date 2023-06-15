package com.zoozo.zoozoforsellers.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;


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

    private  float commission;

    private LocalDate orderDate;

    @Lob
    private String customerDetails;

    @Lob
    private String trackingDetails;
}
