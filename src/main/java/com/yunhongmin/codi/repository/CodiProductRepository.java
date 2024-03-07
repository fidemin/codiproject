package com.yunhongmin.codi.repository;

import com.yunhongmin.codi.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CodiProductRepository extends JpaRepository<CodiProduct, Long> {
    List<CodiProduct> findByCodiBrand(CodiBrand codiBrand);

    @Query(value = """
            SELECT new com.yunhongmin.codi.domain.CodiCategoryStat(
            cp1.codiCategory as codiCategory, MIN(cp1.price) as price, 'MIN' as statType)
                FROM CodiProduct as cp1
                GROUP BY cp1.codiCategory
            """)
    List<CodiCategoryStat> findCodiCategoryStatMin();

    @Query(value = """
            SELECT new com.yunhongmin.codi.domain.CodiCategoryStat(
            cp1.codiCategory as codiCategory, MAX(cp1.price) as price, 'MAX' as statType)
                FROM CodiProduct as cp1
                GROUP BY cp1.codiCategory
            """)
    List<CodiCategoryStat> findCodiCategoryStatMax();

    @Query(value = """
            SELECT cp, cb FROM CodiProduct cp
            JOIN FETCH CodiBrand cb ON cp.codiBrand = cb
            INNER JOIN CodiCategoryStat ccs
            ON cp.codiCategory = ccs.codiCategory AND cp.price = ccs.price WHERE ccs.statType = ?1
            """)
    List<CodiProduct> findCodiProductWithStatType(StatType statType);

    @Query(value = """
            SELECT cp, cb FROM CodiProduct cp
            JOIN FETCH CodiBrand cb ON cp.codiBrand = cb
            INNER JOIN CodiCategoryStat ccs
            ON cp.codiCategory = ccs.codiCategory AND cp.price = ccs.price WHERE ccs.statType = ?1
            AND ccs.codiCategory = ?2
            """)
    List<CodiProduct> findByStatTypeAndCategory(StatType statType, CodiCategory codiCategory);
}
