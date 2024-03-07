package com.yunhongmin.codi.repository;

import com.yunhongmin.codi.domain.CodiBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodiBrandRepository extends JpaRepository<CodiBrand, Long> {
    Optional<CodiBrand> findFirstByOrderByTotalPriceAsc();
}
