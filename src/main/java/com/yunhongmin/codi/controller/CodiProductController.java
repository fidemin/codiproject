package com.yunhongmin.codi.controller;

import com.yunhongmin.codi.dto.CodiProductWithBrandNameDto;
import com.yunhongmin.codi.dto.MinPriceByCategoryDto;
import com.yunhongmin.codi.service.CodiProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/codi/products")
@RequiredArgsConstructor
public class CodiProductController {

    private final CodiProductService codiProductService;

    @RequestMapping(value = "minPriceByCategory", method = RequestMethod.GET)
    public ResponseEntity<MinPriceByCategoryDto> findProductsWithMinPriceByCategory() {
        List<CodiProductWithBrandNameDto> codiProductWithBrandNameDtos =
                codiProductService.findDistinctProductsWithMinPriceByCategory();

        int totalPrice = codiProductWithBrandNameDtos.stream().map(CodiProductWithBrandNameDto::getPrice)
                .mapToInt(price -> price).sum();
        return new ResponseEntity<>(
                new MinPriceByCategoryDto(codiProductWithBrandNameDtos, totalPrice), HttpStatus.OK);
    }

}
