package ro.info.iasi.fiipractic.twitter.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.info.iasi.fiipractic.twitter.exception.UsernameTakenException;

@RestControllerAdvice
public class UsernameTakenAdvice {
    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String usernameTakenHandler(UsernameTakenException ex) {
        return ex.getMessage();
    }
}