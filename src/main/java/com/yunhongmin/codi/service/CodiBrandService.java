package com.yunhongmin.codi.service;

import com.yunhongmin.codi.domain.CodiBrand;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.dto.CodiBrandDto;
import com.yunhongmin.codi.dto.CodiBrandRequestDto;
import com.yunhongmin.codi.dto.CodiCategoryPriceDto;
import com.yunhongmin.codi.exception.NoBrandException;
import com.yunhongmin.codi.repository.CodiBrandRepository;
import com.yunhongmin.codi.repository.CodiProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CodiBrandService {
    private final CodiBrandRepository codiBrandRepository;
    private final CodiProductRepository codiProductRepository;
    private final CodiCategoryService codiCategoryService;

    @Transactional
    public Long createBrand(CodiBrandRequestDto codiBrandCreateDto) {
        String brandName = codiBrandCreateDto.getBrandName();
        CodiBrand codiBrand = new CodiBrand();
        codiBrand.setName(brandName);

        List<CodiCategoryPriceDto> categoryPriceDtos = codiBrandCreateDto.getCategories();

        int totalPrice = categoryPriceDtos.stream().map(CodiCategoryPriceDto::getPrice).mapToInt(p -> p).sum();
        codiBrand.setTotalPrice(totalPrice);

        codiBrandRepository.save(codiBrand);

        for (CodiCategoryPriceDto categoryPriceDto : categoryPriceDtos) {
            CodiProduct codiProduct = new CodiProduct();
            codiProduct.setCodiBrand(codiBrand);
            codiProduct.setPrice(categoryPriceDto.getPrice());
            codiProduct.setCodiCategory(CodiCategory.valueOf(categoryPriceDto.getCategory().toUpperCase()));
            codiProductRepository.save(codiProduct);
        }

        codiCategoryService.updateCodiCategoryStats();
        return codiBrand.getId();
    }

    @Transactional
    public void updateBrand(long brandId, CodiBrandRequestDto codiBrandUpdateDto) {
        Optional<CodiBrand> codiBrandOptional = codiBrandRepository.findById(brandId);
        if (codiBrandOptional.isEmpty()) {
            throw new NoBrandException(
                    String.format("codi brand with brand id %d DOES NOT exist", brandId));
        }

        CodiBrand codiBrand = codiBrandOptional.get();
        codiBrand.setName(codiBrandUpdateDto.getBrandName());

        List<CodiCategoryPriceDto> categoryPriceDtos = codiBrandUpdateDto.getCategories();
        int totalPrice = categoryPriceDtos.stream().map(CodiCategoryPriceDto::getPrice).mapToInt(p -> p).sum();
        codiBrand.setTotalPrice(totalPrice);

        codiBrandRepository.save(codiBrand);

        List<CodiProduct> oldCodiProducts = codiProductRepository.findByCodiBrand(codiBrand);

        Map<CodiCategory, CodiProduct> oldCodiProductMap = oldCodiProducts.stream()
                .collect(Collectors.toMap(CodiProduct::getCodiCategory, cp -> cp));

        for (CodiCategoryPriceDto categoryPriceDto : categoryPriceDtos) {
            CodiCategory codiCategory = CodiCategory.valueOf(categoryPriceDto.getCategory().toUpperCase());
            CodiProduct oldCodiProduct = oldCodiProductMap.remove(codiCategory);

            if (Objects.isNull(oldCodiProduct)) {
                // 기존 CodiProduct 레코드가 존재하지 않으므로 새로 생성
                CodiProduct codiProduct = new CodiProduct();
                codiProduct.setCodiBrand(codiBrand);
                codiProduct.setPrice(categoryPriceDto.getPrice());
                codiProduct.setCodiCategory(codiCategory);
                codiProductRepository.save(codiProduct);
            } else {
                // 기존 CodiProduct 레코드가 존재하면 업데이트
                oldCodiProduct.setPrice(categoryPriceDto.getPrice());
                codiProductRepository.save(oldCodiProduct);
            }
        }

        // oldCodiProductMap에 남은 CodiProduct는 categoryPriceDtos에 없는 카테고리 종류이므로 삭제한다.
        Collection<CodiProduct> codiProductsToDelete = oldCodiProductMap.values();

        for (CodiProduct codiProduct : codiProductsToDelete) {
            codiProductRepository.delete(codiProduct);
        }

        codiCategoryService.updateCodiCategoryStats();
    }

    public Optional<CodiBrandDto> getCodiBrandWithMinTotalPrice() {
        Optional<CodiBrand> codiBrandOptional = codiBrandRepository.findFirstByOrderByTotalPriceAsc();
        if (codiBrandOptional.isEmpty()) {
            return Optional.empty();
        }

        CodiBrand codiBrand = codiBrandOptional.get();
        List<CodiProduct> codiProducts = codiProductRepository.findByCodiBrand(codiBrand);
        List<CodiCategoryPriceDto> codiCategoryPriceDtos = codiProducts.stream()
                .map(codiProduct ->
                        new CodiCategoryPriceDto(
                                codiProduct.getCodiCategory().toString(), codiProduct.getPrice()))
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
