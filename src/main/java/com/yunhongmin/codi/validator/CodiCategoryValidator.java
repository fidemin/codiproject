package com.yunhongmin.codi.validator;

import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.exception.CodiCategoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CodiCategoryValidator {
    public void validateCategories(List<String> categoryStrings) {
        boolean allUnique = categoryStrings.stream().distinct().count() == categoryStrings.size();
        if (!allUnique) {
            throw new CodiCategoryException("all categories must be unique");
        }

        Set<CodiCategory> givenCategorySet = new HashSet<>();
        for (String categoryString : categoryStrings) {
            CodiCategory codiCategory;
            try {
                codiCategory = CodiCategory.valueOf(categoryString.toUpperCase());
            } catch (IllegalArgumentException ex) {
                log.warn(ex.getMessage());
                throw new CodiCategoryException(String.format("%s is not proper category", categoryString));
            }
            givenCategorySet.add(codiCategory);
        }

        Set<CodiCategory> originalSet = Arrays.stream(CodiCategory.values()).collect(Collectors.toSet());
        originalSet.removeAll(givenCategorySet);

        if (!originalSet.isEmpty()) {
            String missCategoriesStr = originalSet.stream()
                    .map(cate -> cate.toString().toLowerCase()).collect(Collectors.joining(", "));
            String errorMessage = String.format("missed categories: %s", missCategoriesStr);
            throw new CodiCategoryException(errorMessage);
        }
    }
}
