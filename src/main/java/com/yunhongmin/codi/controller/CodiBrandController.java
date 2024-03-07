package com.yunhongmin.codi.controller;

import com.yunhongmin.codi.dto.CodiBrandDto;
import com.yunhongmin.codi.service.CodiBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/codi/brands")
@RequiredArgsConstructor
public class CodiBrandController {
    private final CodiBrandService codiBrandService;

    @RequestMapping(method = RequestMethod.GET, value = "withMinPrice")
    ResponseEntity<CodiBrandDto> getBrandWithMinPrice() {
        Optional<CodiBrandDto> codiBrandOptional = codiBrandService.getCodiBrandWithMinTotalPrice();
        if (codiBrandOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(codiBrandOptional.get());
    }
}
