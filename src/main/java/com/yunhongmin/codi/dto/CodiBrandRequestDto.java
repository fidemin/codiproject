package com.yunhongmin.codi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CodiBrandRequestDto {
    @Size(min = 1, max = 255, message = "brandName must be between 1 and 255 characters long")
    @NotNull
    private String brandName;

    @NotNull
    @NotEmpty(message = "no categories given")
    @Valid
    private List<CodiCategoryPriceDto> categories;
}
