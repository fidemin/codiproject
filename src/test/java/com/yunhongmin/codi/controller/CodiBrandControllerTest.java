package com.yunhongmin.codi.controller;

import com.yunhongmin.codi.common.CommonResponseDto;
import com.yunhongmin.codi.dto.CodiBrandDto;
import com.yunhongmin.codi.dto.CodiBrandIdDto;
import com.yunhongmin.codi.dto.CodiBrandRequestDto;
import com.yunhongmin.codi.dto.CodiCategoryPriceDto;
import com.yunhongmin.codi.exception.BadRequestException;
import com.yunhongmin.codi.exception.CodiCategoryException;
import com.yunhongmin.codi.exception.NoBrandException;
import com.yunhongmin.codi.service.CodiBrandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodiBrandControllerTest {
    @InjectMocks
    private CodiBrandController codiBrandController;

    @Mock
    private CodiBrandService codiBrandService;

    @Test
    @DisplayName("createBrand 성공")
    void createBrand() {
        // given
        CodiBrandRequestDto codiBrandRequestDto = new CodiBrandRequestDto();

        Long brandId = 1L;
        when(codiBrandService.createBrand(codiBrandRequestDto)).thenReturn(brandId);


        // when
        ResponseEntity<CommonResponseDto<CodiBrandIdDto>> responseEntity
                = codiBrandController.createBrand(codiBrandRequestDto);
        CommonResponseDto<CodiBrandIdDto> body = responseEntity.getBody();

        // then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(true, body.getSuccess());
        assertNull(body.getErrorMessage());
        assertEquals(brandId, body.getData().getBrandId());
    }

    @Test
    @DisplayName("createBrand 실패: CodiCategoryException 케이스")
    void createBrand_CodiCategoryException() {
        // given
        CodiBrandRequestDto codiBrandRequestDto = new CodiBrandRequestDto();

        when(codiBrandService.createBrand(codiBrandRequestDto)).thenThrow(CodiCategoryException.class);

        // when
        assertThrows(BadRequestException.class, () -> codiBrandController.createBrand(codiBrandRequestDto));
    }

    @Test
    @DisplayName("updateBrand 성공")
    void updateBrand() {
        // given
        CodiBrandRequestDto codiBrandRequestDto = new CodiBrandRequestDto();

        Long brandId = 1L;
        doNothing().when(codiBrandService).updateBrand(brandId, codiBrandRequestDto);

        // when
        ResponseEntity<CommonResponseDto<Void>> responseEntity
                = codiBrandController.updateBrand(brandId, codiBrandRequestDto);
        CommonResponseDto<Void> body = responseEntity.getBody();

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, body.getSuccess());
        assertNull(body.getErrorMessage());
        assertNull(body.getData());
    }

    @Test
    @DisplayName("updateBrand 실패: NoBrandException 케이스")
    void updateBrand_NoBrandException() {
        // given
        CodiBrandRequestDto codiBrandRequestDto = new CodiBrandRequestDto();

        Long brandId = 1L;
        String errorMessage = "codi brand with brand id 1 DOES NOT exist";
        doThrow(new NoBrandException(errorMessage)).when(codiBrandService).updateBrand(brandId, codiBrandRequestDto);

        // when
        ResponseEntity<CommonResponseDto<Void>> responseEntity
                = codiBrandController.updateBrand(brandId, codiBrandRequestDto);
        CommonResponseDto<Void> body = responseEntity.getBody();

        // then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(false, body.getSuccess());
        assertEquals(errorMessage, body.getErrorMessage());
        assertNull(body.getData());
    }

    @Test
    @DisplayName("updateBrand 실패: CodiCategoryException 케이스")
    void updateBrand_CodiCategoryException() {
        // given
        CodiBrandRequestDto codiBrandRequestDto = new CodiBrandRequestDto();

        Long brandId = 1L;
        doThrow(CodiCategoryException.class)
                .when(codiBrandService).updateBrand(brandId, codiBrandRequestDto);

        // expected
        assertThrows(BadRequestException.class, () -> codiBrandController.updateBrand(brandId, codiBrandRequestDto));
    }

    @Test
    @DisplayName("deleteBrand 성공")
    void deleteBrand() {
        // given
        CodiBrandRequestDto codiBrandRequestDto = new CodiBrandRequestDto();

        Long brandId = 1L;
        doNothing().when(codiBrandService).deleteBrand(brandId);

        // when
        ResponseEntity<CommonResponseDto<Void>> responseEntity
                = codiBrandController.deleteBrand(brandId);
        CommonResponseDto<Void> body = responseEntity.getBody();

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, body.getSuccess());
        assertNull(body.getErrorMessage());
        assertNull(body.getData());
    }

    @Test
    @DisplayName("getBrandWithMinPrice method 성공 케이스")
    void getBrandWithMinPrice() {
        // given
        List<CodiCategoryPriceDto> list = new ArrayList<>();
        CodiBrandDto codiBrandDto = CodiBrandDto.builder()
                .brandName("A").brandId(1L).totalPrice(0).categories(list)
                .build();
        when(codiBrandService.getCodiBrandWithMinTotalPrice()).thenReturn(Optional.of(codiBrandDto));

        // when
        ResponseEntity<CommonResponseDto<CodiBrandDto>> responseEntity
                = codiBrandController.getBrandWithMinPrice();
        CommonResponseDto<CodiBrandDto> body = responseEntity.getBody();

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, body.getSuccess());
        assertNull(body.getErrorMessage());
        assertEquals(codiBrandDto, body.getData());
    }

    @Test
    @DisplayName("getBrandWithMinPrice method 실패 케이스: brand가 존재 안함")
    void getBrandWithMinPrice_NoBrand() {
        // given
        when(codiBrandService.getCodiBrandWithMinTotalPrice()).thenReturn(Optional.empty());

        // when
        ResponseEntity<CommonResponseDto<CodiBrandDto>> responseEntity
                = codiBrandController.getBrandWithMinPrice();
        CommonResponseDto<CodiBrandDto> body = responseEntity.getBody();

        // then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(false, body.getSuccess());
        assertEquals("There is no codi brand available", body.getErrorMessage());
        assertNull(body.getData());

    }
}