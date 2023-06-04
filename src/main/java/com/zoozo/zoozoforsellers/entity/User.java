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
public class User {

    @Id
    private String nic;

    private String username;

    private String password;

    private String mobileNo;

    @Lob
    private String bankDetails;

    private String role;
}
