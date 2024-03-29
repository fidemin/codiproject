package com.yunhongmin.codi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "codi_products")
@Getter
@Setter
public class CodiProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "codi_brand_id", nullable = false)
    private CodiBrand codiBrand;

    @Column(name = "codi_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private CodiCategory codiCategory;

    private int price;
}
