package com.example.template.advice;

import com.example.template.advice.exception.*;
import com.example.template.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<Object>();
        apiResponse.setCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicate(DuplicateRecordException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.CONFLICT.value());
        apiResponse.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }

    @ExceptionHandler(IdInvalidException.class)
    public ResponseEntity<ApiResponse<Object>> handleIdInvalid(IdInvalidException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.BAD_REQUEST.value()); // ID sai → 400
        apiResponse.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NotFoundException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.NOT_FOUND.value()); // 404
        apiResponse.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ApiResponse<Object>> handlePermission(PermissionException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.FORBIDDEN.value()); // 403 Forbidden
        apiResponse.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.NOT_FOUND.value());
        apiResponse.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleRateLimit(RateLimitExceededException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.TOO_MANY_REQUESTS.value());
        apiResponse.setMessage(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(apiResponse);
    }

    // Handler fallback cho các lỗi không được bắt riêng
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiResponse.setMessage("Internal server error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }


}


