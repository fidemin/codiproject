package com.yunhongmin.codi.common;

import com.yunhongmin.codi.exception.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<CommonResponseDto<Void>> handleException(MethodArgumentNotValidException ex) {
        ObjectError objectError = ex.getBindingResult().getAllErrors().get(0);
        String errorMessage = objectError.getDefaultMessage();
        return ResponseEntity.badRequest().body(CommonResponseDto.ofFail(errorMessage));
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<CommonResponseDto<Void>> handleException(BadRequestException ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.badRequest().body(CommonResponseDto.ofFail(errorMessage));
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<CommonResponseDto<Void>> handleException(EntityNotFoundException ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonResponseDto.ofFail(errorMessage));
    }

}
