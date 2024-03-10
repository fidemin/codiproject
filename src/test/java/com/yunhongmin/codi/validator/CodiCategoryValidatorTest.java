package com.yunhongmin.codi.validator;

import com.yunhongmin.codi.exception.CodiCategoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CodiCategoryValidatorTest {
    @InjectMocks
    private CodiCategoryValidator codiCategoryValidator;

    @Test
    @DisplayName("valdiateCategories 성공")
    void validateCategories() {
        List<String> categoryStrings = new ArrayList<>();
        categoryStrings.add("top");
        categoryStrings.add("outerware");
        categoryStrings.add("pants");
        categoryStrings.add("sneakers");
        categoryStrings.add("bag");
        categoryStrings.add("hat");
        categoryStrings.add("socks");
        categoryStrings.add("accessory");

        // expect no exception
        codiCategoryValidator.validateCategories(categoryStrings);
    }

    @Test
    @DisplayName("valdiateCategories 실패: 중복 카테고리")
    void validateCategories_duplicate() {
        List<String> categoryStrings = new ArrayList<>();
        categoryStrings.add("top");
        categoryStrings.add("top");
        categoryStrings.add("pants");
        categoryStrings.add("sneakers");
        categoryStrings.add("bag");
        categoryStrings.add("hat");
        categoryStrings.add("socks");
        categoryStrings.add("accessory");

        // expect
        assertThrows(CodiCategoryException.class, () -> codiCategoryValidator.validateCategories(categoryStrings));
    }

    @Test
    @DisplayName("valdiateCategories 실패: 잘못된 카테고리")
    void validateCategories_wrongCategory() {
        List<String> categoryStrings = new ArrayList<>();
        categoryStrings.add("top");
        categoryStrings.add("outerware");
        categoryStrings.add("pants");
        categoryStrings.add("sneakers");
        categoryStrings.add("bag1"); // bag1 is wrong category
        categoryStrings.add("hat");
        categoryStrings.add("socks");
        categoryStrings.add("accessory");

        // expect
        CodiCategoryException codiCategoryException =
                assertThrows(CodiCategoryException.class,
                        () -> codiCategoryValidator.validateCategories(categoryStrings));

        // then
        assertEquals("bag1 is not proper category", codiCategoryException.getMessage());
    }

    @Test
    @DisplayName("valdiateCategories 실패: 카테고리 누락")
    void validateCategories_missingCategory() {
        List<String> categoryStrings = new ArrayList<>();
        // bag category 누락
        categoryStrings.add("top");
        categoryStrings.add("outerware");
        categoryStrings.add("pants");
        categoryStrings.add("sneakers");
        categoryStrings.add("hat");
        categoryStrings.add("socks");
        categoryStrings.add("accessory");

        // expect
        CodiCategoryException codiCategoryException =
                assertThrows(CodiCategoryException.class,
                        () -> codiCategoryValidator.validateCategories(categoryStrings));

        // then
        assertEquals("missed categories: bag", codiCategoryException.getMessage());
    }

    @Test
    @DisplayName("valdiateCategory 성공")
    void validateCategory() {
        // expect
        codiCategoryValidator.validateCategory("top");
    }

    @Test
    @DisplayName("valdiateCategory 실패")
    void validateCategory_wrongCategory() {
        // expect
        assertThrows(CodiCategoryException.class,
                () -> codiCategoryValidator.validateCategory("top1"));
    }
}