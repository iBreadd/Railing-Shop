//package com.example.RailingShop.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//
//@ControllerAdvice
//public class AppExceptionHandler {
//
//    @ExceptionHandler(FuncErrorException.class)
//    public ResponseEntity<Object> handleFuncErrorException(FuncErrorException ex, WebRequest request) {
//        String message = ex.getMessage();
//        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
//        String message = ex.getMessage();
//        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
//        String message = "An unexpected error occurred";
//        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}
