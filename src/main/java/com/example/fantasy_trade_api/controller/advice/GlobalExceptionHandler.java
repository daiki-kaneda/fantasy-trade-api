package com.example.fantasy_trade_api.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.google.firebase.auth.FirebaseAuthException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FirebaseAuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthError(FirebaseAuthException e) {
        var error = new ErrorResponse("AUTH_ERROR", "認証に失敗しました" + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    // ビジネスロジックエラー (残高不足、自分への送金など)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException e) {
        var error = new ErrorResponse("BAD_REQUEST", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // データが見つからないエラー
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException e) {
        var error = new ErrorResponse("NOT_FOUND", "対象のデータが見つかりません");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalError(EntityNotFoundException e) {
        var error = new ErrorResponse("INTERNAL_ERROR", "予期せぬエラーが発生しました");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    public record ErrorResponse(String code, String message) {
    }
}
