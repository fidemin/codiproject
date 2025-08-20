package com.yunhongmin.codi.service;

import com.yunhongmin.codi.domain.CodiBrand;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.dto.CodiBrandDto;
import com.yunhongmin.codi.dto.CodiBrandRequestDto;
import com.yunhongmin.codi.dto.CodiCategoryPriceDto;
import com.yunhongmin.codi.exception.CodiCategoryException;
import com.yunhongmin.codi.repository.CodiBrandRepository;
import com.yunhongmin.codi.repository.CodiProductRepository;
import com.yunhongmin.codi.validator.CodiCategoryValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodiBrandServiceTest {
    @InjectMocks
    private CodiBrandService codiBrandService;
    @Mock
    private CodiBrandRepository codiBrandRepository;
    @Mock
    private CodiProductRepository codiProductRepository;
    @Mock
    private CodiCategoryService codiCategoryService;
    @Mock
    private CodiCategoryValidator codiCategoryValidator;

    @Test
    @DisplayName("CodiBrand 생성 성공")
    void createBrand() {
        CodiBrandRequestDto dto = new CodiBrandRequestDto();

        List<CodiCategoryPriceDto> categories = new ArrayList<>();
        categories.add(new CodiCategoryPriceDto("top", 1000));
        categories.add(new CodiCategoryPriceDto("sneakers", 2000));

        dto.setCategories(categories);

        Long brandId = 1L;

        ArgumentCaptor<CodiBrand> captor = ArgumentCaptor.forClass(CodiBrand.class);
        doAnswer((Answer<Void>) invocation -> {
            CodiBrand codiBrand = invocation.getArgument(0);
            codiBrand.setId(brandId); // Modify the argument as needed
            return null;
        }).when(codiBrandRepository).save(captor.capture());

        // when
        Long actualBrandId = codiBrandService.createBrand(dto);

        // then
        assertEquals(brandId, actualBrandId);
        verify(codiCategoryValidator, times(1)).validateCategories(anyList());
        verify(codiBrandRepository, times(1)).save(any());
        verify(codiProductRepository, times(categories.size())).save(any());
        verify(codiCategoryService, times(1)).updateCodiCategoryStats();
    }

    @Test
    @DisplayName("CodiBrand 생성 실패: validation fail")
    void createBrand_validationFail() {
        CodiBrandRequestDto dto = new CodiBrandRequestDto();
        dto.setCategories(new ArrayList<>());

        doThrow(CodiCategoryException.class).when(codiCategoryValidator).validateCategories(anyList());

        // expect
        assertThrows(CodiCategoryException.class, () -> codiBrandService.createBrand(dto));
    }

    @Test
    @DisplayName("Codi Brand 업데이트 성공")
    void updateBrand() {
        CodiBrandRequestDto dto = new CodiBrandRequestDto();

        List<CodiCategoryPriceDto> categories = new ArrayList<>();
        categories.add(new CodiCategoryPriceDto("top", 1000));
        categories.add(new CodiCategoryPriceDto("sneakers", 2000));

        dto.setCategories(categories);

        Long brandId = 1L;

        List<CodiProduct> returnedCodiProducts = new ArrayList<>();
        CodiProduct codiProduct1 = new CodiProduct();
        codiProduct1.setCodiCategory(CodiCategory.TOP); // will be updated

        CodiProduct codiProduct2 = new CodiProduct();
        codiProduct2.setCodiCategory(CodiCategory.BAG); // will be deleted.

        CodiProduct codiProduct3 = new CodiProduct();
        codiProduct3.setCodiCategory(CodiCategory.PANTS); // will be deleted.

        returnedCodiProducts.add(codiProduct1);
        returnedCodiProducts.add(codiProduct2);
        returnedCodiProducts.add(codiProduct3);

        when(codiProductRepository.findByCodiBrand(any())).thenReturn(returnedCodiProducts);
        when(codiBrandRepository.findById(brandId)).thenReturn(Optional.of(new CodiBrand()));

        // when
        codiBrandService.updateBrand(brandId, dto);

        // then
        verify(codiCategoryValidator, times(1)).validateCategories(anyList());
        verify(codiBrandRepository, times(1)).save(any());
        verify(codiProductRepository, times(2)).save(any());
        verify(codiProductRepository, times(2)).delete(any());
        verify(codiCategoryService, times(1)).updateCodiCategoryStats();
    }

    @Test
    @DisplayName("Codi Brand 업데이트 실패: 브랜드 존재 않함")
    void updateBrand_NoBrand() {
        CodiBrandRequestDto dto = new CodiBrandRequestDto();
        List<CodiCategoryPriceDto> categories = new ArrayList<>();
        dto.setCategories(categories);

        long brandId = 1L;

        when(codiBrandRepository.getReferenceById(brandId)).thenThrow(new EntityNotFoundException());

        // expect
        assertThrows(EntityNotFoundException.class, () -> codiBrandService.updateBrand(brandId, dto));

        // then
        verify(codiCategoryValidator, times(1)).validateCategories(anyList());
    }

    @Test
    @DisplayName("Codi Brand 업데이트 실패: validation fail")
    void updateBrand_validationFail() {
        CodiBrandRequestDto dto = new CodiBrandRequestDto();
        List<CodiCategoryPriceDto> categories = new ArrayList<>();
        dto.setCategories(categories);

        doThrow(CodiCategoryException.class).when(codiCategoryValidator).validateCategories(anyList());

        // expect
        assertThrows(CodiCategoryException.class, () -> codiBrandService.updateBrand(1L, dto));
    }

    @Test
    @DisplayName("Codi Brand 삭제 성공")
    void deleteBrand() {
        // given
        long brandId = 1L;
        when(codiBrandRepository.findById(brandId)).thenReturn(Optional.of(new CodiBrand()));

        // when
        codiBrandService.deleteBrand(brandId);

        // then
        verify(codiProductRepository, times(1)).deleteBulkByCodiBrand(any());

        verify(codiBrandRepository, times(1)).delete(any());
        verify(codiCategoryService, times(1)).updateCodiCategoryStats();
    }

    @Test
    @DisplayName("Codi Brand 삭제 성공: No Brand")
    void deleteBrand_noBrand() {
        // given
        long brandId = 1L;
        when(codiBrandRepository.findById(brandId)).thenReturn(Optional.empty());

        // when
        codiBrandService.deleteBrand(brandId);

        // then
        verify(codiProductRepository, times(0)).deleteBulkByCodiBrand(any());

        verify(codiBrandRepository, times(0)).delete(any());
        verify(codiCategoryService, times(0)).updateCodiCategoryStats();
    }

    @Test
    @DisplayName("getCodiBrandWithMinTotalPrice 성공")
    void getCodiBrandWithMinTotalPrice() {
        // given
        CodiBrand codiBrand = new CodiBrand();
        codiBrand.setId(1L);
        codiBrand.setName("A");
        when(codiBrandRepository.findFirstByOrderByTotalPriceAsc()).thenReturn(Optional.of(codiBrand));


        List<CodiProduct> codiProducts = new ArrayList<>();
        CodiProduct codiProduct1 = new CodiProduct();
        codiProduct1.setCodiCategory(CodiCategory.TOP);
        codiProduct1.setPrice(1000);
        CodiProduct codiProduct2 = new CodiProduct();
        codiProduct2.setCodiCategory(CodiCategory.BAG);
        codiProduct2.setPrice(2000);
        codiProducts.add(codiProduct1);
        codiProducts.add(codiProduct2);

        when(codiProductRepository.findByCodiBrand(codiBrand)).thenReturn(codiProducts);

        // when
        Optional<CodiBrandDto> actualOptional = codiBrandService.getCodiBrandWithMinTotalPrice();

        // then
        assertTrue(actualOptional.isPresent());
        CodiBrandDto codiBrandDto = actualOptional.get();
        assertEquals(1L, codiBrandDto.getBrandId());
        assertEquals("A", codiBrandDto.getBrandName());
        assertEquals(3000, codiBrandDto.getTotalPrice());
        List<CodiCategoryPriceDto> categories = codiBrandDto.getCategories();
        assertEquals(codiProducts.size(), categories.size());
        assertEquals(codiProducts.get(0).getCodiCategory().toString(), categories.get(0).getCategory());
        assertEquals(codiProducts.get(0).getPrice(), categories.get(0).getPrice());
        assertEquals(codiProducts.get(1).getCodiCategory().toString(), categories.get(1).getCategory());
        assertEquals(codiProducts.get(1).getPrice(), categories.get(1).getPrice());
    }

    @Test
    @DisplayName("getCodiBrandWithMinTotalPrice 성공: 브랜드 존재 안함")
    void getCodiBrandWithMinTotalPrice_noBrand() {
        // given
        when(codiBrandRepository.findFirstByOrderByTotalPriceAsc()).thenReturn(Optional.empty());

        // when
        Optional<CodiBrandDto> actualOptional = codiBrandService.getCodiBrandWithMinTotalPrice();

        // then
        assertTrue(actualOptional.isEmpty());
    }
}