package com.yunhongmin.codi.repository;

import com.yunhongmin.codi.domain.CodiCategoryStat;
import com.yunhongmin.codi.domain.StatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CodiCategoryStatRepository extends JpaRepository<CodiCategoryStat, Long> {
    List<CodiCategoryStat> findByStatTypeIn(List<StatType> statTypes);

    @Query("DELETE FROM CodiCategoryStat ccs WHERE ccs.statType in :statTypes")
    @Modifying
    @Transactional
    void deleteBulkByStatTypes(List<StatType> statTypes);
}
