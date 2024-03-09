package com.yunhongmin.codi.common;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponseDto<Void>> handleException(MethodArgumentNotValidException ex) {
        ObjectError objectError = ex.getBindingResult().getAllErrors().get(0);
        String errorMessage = objectError.getDefaultMessage();
        return ResponseEntity.badRequest().body(CommonResponseDto.ofFail(errorMessage));
    }

}
