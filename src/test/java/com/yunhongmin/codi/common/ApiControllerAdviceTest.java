package com.yunhongmin.codi.common;

import com.yunhongmin.codi.exception.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ApiControllerAdviceTest {
    @InjectMocks
    private ApiControllerAdvice apiControllerAdvice;

    @Test
    void handleException_MethodArgumentNotValidException() {
        // given
        String errorMsg = "error meessage";
        MethodArgumentNotValidException exception = createMethodArgumentNotValidException(errorMsg);

        // when
        ResponseEntity<CommonResponseDto<Void>> responseEntity = apiControllerAdvice.handleException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMsg, responseEntity.getBody().getErrorMessage());
    }

    private MethodArgumentNotValidException createMethodArgumentNotValidException(String errorMessage) {
        FieldError error = new FieldError("objectName", "anyField", errorMessage);
        BindingResult bindingResult
                = new org.springframework.validation.BeanPropertyBindingResult(null, null);
        bindingResult.addError(error);
        return new MethodArgumentNotValidException(null, bindingResult);
    }

    @Test
    void testHandleException_BadRequestException() {
        // given
        String errorMsg = "error meessage";
        BadRequestException exception = new BadRequestException(errorMsg);

        // when
        ResponseEntity<CommonResponseDto<Void>> responseEntity = apiControllerAdvice.handleException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMsg, responseEntity.getBody().getErrorMessage());
    }

    @Test
    void testHandleException_EntityNotFoundException() {
        // given
        String errorMsg = "error meessage";
        EntityNotFoundException exception = new EntityNotFoundException(errorMsg);

        // when
        ResponseEntity<CommonResponseDto<Void>> responseEntity = apiControllerAdvice.handleException(exception);

        // then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(errorMsg, responseEntity.getBody().getErrorMessage());
    }
}