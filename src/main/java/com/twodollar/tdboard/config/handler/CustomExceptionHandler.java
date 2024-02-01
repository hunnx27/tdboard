package com.twodollar.tdboard.config.handler;

import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler
    public ResponseEntity accessDeniedException(AccessDeniedException e) throws AccessDeniedException{
        log.info(e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error(HttpStatus.FORBIDDEN.toString(), "로그인 후 이용해주세요."));
    }
}
