package com.yunhongmin.codi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "codi_category_stat")
@Getter
@Setter
public class CodiCategoryStat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "code_brand_id")
    private CodiBrand codiBrand;

    private int price;

    @Enumerated(EnumType.STRING)
    private StatType statType;
}
