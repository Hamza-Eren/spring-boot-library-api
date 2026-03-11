package com.hamzaeren.course.spring.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// 
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "İşlem başarısız",
                "message", e.getMessage()));
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Doğrulanamadı",
                "message", errorMessage));
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(
            org.springframework.dao.DataIntegrityViolationException e) {
        // Exception mesajını küçük harfe çevirerek hangi alanın çakıştığını bulmaya
        // çalış
        String exceptionMessage = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        String message;
        if (exceptionMessage.contains("key (email)")) {
            message = "Bu e-posta adresi zaten kullanımda";
        } else if (exceptionMessage.contains("key (username)")) {
            message = "Bu kullanıcı adı zaten kullanımda";
        } else if (exceptionMessage.contains("name")) {
            message = "Bu isim zaten kayıtlı";
        } else if (exceptionMessage.contains("isbn")) {
            message = "Bu ISBN numarası zaten kayıtlı";
        } else {
            message = "Bu bilgi zaten kullanımda";
        }
        return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).body(Map.of(
                "error", "Veri çakışması",
                "message", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Beklenmedik bir hata",
                "message", e.getMessage()));
    }
}
