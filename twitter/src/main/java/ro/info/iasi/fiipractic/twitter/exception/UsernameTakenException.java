package ro.info.iasi.fiipractic.twitter.exception;

public class UsernameTakenException extends RuntimeException{

    public UsernameTakenException(String message) {
        super(message);
    }
}
