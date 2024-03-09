package com.yunhongmin.codi.controller;

import com.yunhongmin.codi.common.CommonResponseDto;
import com.yunhongmin.codi.dto.CodiBrandDto;
import com.yunhongmin.codi.dto.CodiBrandIdDto;
import com.yunhongmin.codi.dto.CodiBrandRequestDto;
import com.yunhongmin.codi.exception.NoBrandException;
import com.yunhongmin.codi.service.CodiBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/codi/brands")
@RequiredArgsConstructor
@Slf4j
public class CodiBrandController {
    private final CodiBrandService codiBrandService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CommonResponseDto<CodiBrandIdDto>> createBrand(
            @RequestBody CodiBrandRequestDto codiBrandCreateDto) {
        Long brandId = codiBrandService.createBrand(codiBrandCreateDto);
        return ResponseEntity.ok(CommonResponseDto.ofSuccess(new CodiBrandIdDto(brandId)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{brandId}")
    public ResponseEntity<CommonResponseDto<Void>> updateBrand(
            @PathVariable("brandId") Long brandId,
            @RequestBody CodiBrandRequestDto codiBrandUpdateDto) {
        try {
            codiBrandService.updateBrand(brandId, codiBrandUpdateDto);
        } catch (NoBrandException ex) {
            log.warn(ex.getMessage());
            return new ResponseEntity<>(CommonResponseDto.ofFail(ex.getMessage()), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(CommonResponseDto.ofSuccess());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{brandId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteBrand(
            @PathVariable("brandId") Long brandId) {
        codiBrandService.deleteBrand(brandId);
        return ResponseEntity.ok(CommonResponseDto.ofSuccess());
    }


    @RequestMapping(method = RequestMethod.GET, value = "withMinPrice")
    public ResponseEntity<CommonResponseDto<CodiBrandDto>> getBrandWithMinPrice() {
        Optional<CodiBrandDto> codiBrandOptional = codiBrandService.getCodiBrandWithMinTotalPrice();
        if (codiBrandOptional.isEmpty()) {
            return new ResponseEntity<>(
                    CommonResponseDto.ofFail("There is no codi brand available"),
                    HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(CommonResponseDto.ofSuccess(codiBrandOptional.get()));
    }
}
