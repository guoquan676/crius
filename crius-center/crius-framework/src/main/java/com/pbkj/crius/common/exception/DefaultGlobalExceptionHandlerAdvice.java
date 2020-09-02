package com.pbkj.crius.common.exception;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class DefaultGlobalExceptionHandlerAdvice {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleRuntimeException(HttpServletRequest request, RuntimeException ex) {
        log.error("access {}, URL-params:{}, body-params:{},  access ip:{}",
                request.getRequestURI(), request.getQueryString(),
                JSONUtil.parseObj(request.getParameterMap()), request.getRemoteAddr(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}