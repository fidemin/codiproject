package com.yunhongmin.codi.common;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CommonResponseDto<T> {
    private final boolean success;
    private final String errorMessage;
    private final T data;

    public static <T> CommonResponseDto<T> ofSuccess(T data) {
        return new CommonResponseDto<>(true, null, data);
    }

    public static CommonResponseDto<Void> ofSuccess() {
        return new CommonResponseDto<>(true, null, null);
    }

    public static <T> CommonResponseDto<T> ofFail(String errorMessage) {
        return new CommonResponseDto<>(false, errorMessage, null);
    }
}
