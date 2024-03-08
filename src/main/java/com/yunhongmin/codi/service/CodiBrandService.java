package com.yunhongmin.codi.service;

import com.yunhongmin.codi.domain.CodiBrand;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.dto.CodiBrandCreateDto;
import com.yunhongmin.codi.dto.CodiBrandDto;
import com.yunhongmin.codi.dto.CodiCategoryPriceDto;
import com.yunhongmin.codi.repository.CodiBrandRepository;
import com.yunhongmin.codi.repository.CodiProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodiBrandService {
    private final CodiBrandRepository codiBrandRepository;
    private final CodiProductRepository codiProductRepository;
    private final CodiCategoryService codiCategoryService;

    @Transactional
    public Long createBrand(CodiBrandCreateDto codiBrandCreateDto) {
        String brandName = codiBrandCreateDto.getBrandName();
        CodiBrand codiBrand = new CodiBrand();
        codiBrand.setName(brandName);

        List<CodiCategoryPriceDto> categoryPriceDtos = codiBrandCreateDto.getCategories();

        int totalPrice = categoryPriceDtos.stream().map(dto -> dto.getPrice()).mapToInt(p -> p).sum();
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
