package com.yunhongmin.codi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "codi_category_stats")
@Getter
@Setter
@NoArgsConstructor
public class CodiCategoryStat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "codi_category", nullable = false)
    private CodiCategory codiCategory;
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "stat_type", nullable = false)
    private StatType statType;

    public CodiCategoryStat(CodiCategory codiCategory, int price, String statType) {
        this.codiCategory = codiCategory;
        this.price = price;
        this.statType = StatType.valueOf(statType);
    }
}
