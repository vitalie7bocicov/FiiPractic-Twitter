package ro.info.iasi.fiipractic.twitter.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.info.iasi.fiipractic.twitter.exception.UnauthorizedException;

@RestControllerAdvice
public class UnauthorizedExceptionAdvice {
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String usernameTakenHandler() {
        return "Unauthorized!";
    }
}