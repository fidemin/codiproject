package com.yunhongmin.codi.service;

import com.yunhongmin.codi.converter.CodiProductConverter;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.domain.StatType;
import com.yunhongmin.codi.dto.CodiProductWithBrandNameDto;
import com.yunhongmin.codi.repository.CodiProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CodiProductService {
    private final CodiProductRepository codiProductRepository;

    @Transactional(readOnly = true)
    public List<CodiProductWithBrandNameDto> findAll() {
        List<CodiProduct> codiProducts = codiProductRepository.findAllWithBrand();
        return codiProducts.stream().map(CodiProductConverter::toCodiProductWithBrandNameDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CodiProductWithBrandNameDto> findDistinctProductsWithMinPriceByCategory() {
        List<CodiProduct> codiProducts = codiProductRepository.findCodiProductWithStatType(StatType.MIN);
        HashSet<CodiCategory> seenCategory = new HashSet<>();

        List<CodiProduct> distinctCodiProducts = new ArrayList<>();

        for (CodiProduct codiProduct : codiProducts) {
            if (seenCategory.contains(codiProduct.getCodiCategory())) {
                continue;
            }
            distinctCodiProducts.add(codiProduct);
            seenCategory.add(codiProduct.getCodiCategory());
        }

        return distinctCodiProducts.stream()
                .map(CodiProductConverter::toCodiProductWithBrandNameDto).collect(Collectors.toList());
    }
}
