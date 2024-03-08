package com.yunhongmin.codi.repository;

import com.yunhongmin.codi.domain.CodiCategoryStat;
import com.yunhongmin.codi.domain.StatType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodiCategoryStatRepository extends JpaRepository<CodiCategoryStat, Long> {
    List<CodiCategoryStat> findByStatTypeIn(List<StatType> statTypes);

}
