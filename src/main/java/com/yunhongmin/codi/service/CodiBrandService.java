package com.yunhongmin.codi.service;

import com.yunhongmin.codi.domain.CodiBrand;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.dto.CodiBrandDto;
import com.yunhongmin.codi.dto.CodiCategoryPriceDto;
import com.yunhongmin.codi.repository.CodiBrandRepository;
import com.yunhongmin.codi.repository.CodiProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodiBrandService {
    private final CodiBrandRepository codiBrandRepository;
    private final CodiProductRepository codiProductRepository;

    public Optional<CodiBrandDto> getCodiBrandWithMinTotalPrice() {
        Optional<CodiBrand> codiBrandOptional = codiBrandRepository.findFirstByOrderByTotalPriceAsc();
        if (codiBrandOptional.isEmpty()) {
            return Optional.empty();
        }

        CodiBrand codiBrand = codiBrandOptional.get();
        List<CodiProduct> codiProducts = codiProductRepository.findByCodiBrand(codiBrand);
        List<CodiCategoryPriceDto> codiCategoryPriceDtos = codiProducts.stream()
                .map(codiProduct -> new CodiCategoryPriceDto(codiProduct.getCodiCategory(), codiProduct.getPrice()))
                .toList();
        int totalPrice = codiProducts.stream().map(CodiProduct::getPrice).mapToInt(price -> price).sum();

        CodiBrandDto codiBrandDto = CodiBrandDto.builder()
                .brandId(codiBrand.getId())
                .brandName(codiBrand.getName())
                .categories(codiCategoryPriceDtos)
                .totalPrice(totalPrice)
                .build();
        return Optional.of(codiBrandDto);
    }
}
