package com.yunhongmin.codi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "code_brands")
@Getter
@Setter
public class CodiBrand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @Column(name = "total_price")
    private int totalPrice;
}
