package online.referity.exception.exceptions;

public class ExpiredToken  extends RuntimeException{
    public ExpiredToken(String message) {
        super(message);
    }
}
