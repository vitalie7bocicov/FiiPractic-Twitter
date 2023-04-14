package ro.info.iasi.fiipractic.twitter.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.info.iasi.fiipractic.twitter.exception.FollowRelationshipNotFound;

@RestControllerAdvice
public class FollowRelationshipNotFoundAdvice {

    @ExceptionHandler(FollowRelationshipNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequestHandler(FollowRelationshipNotFound ex) {
        return ex.getMessage();
    }
}
