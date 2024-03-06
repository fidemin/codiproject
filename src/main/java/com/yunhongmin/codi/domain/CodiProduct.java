package com.yunhongmin.codi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "code_products")
@Getter
@Setter
public class CodiProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "codi_brand_id")
    private CodiBrand codiBrand;

    @Column(name = "codi_category")
    @Enumerated(EnumType.STRING)
    private CodiCategory codiCategory;

    private int price;
}
