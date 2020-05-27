package exceptions;

public class ValidatorException extends Exception {
    private String msg;

    public ValidatorException(String message) {
        super(message);
        msg = message;
    }

    public String getMessage() {
        return msg;
    }

    public String toString() {
        return super.toString();
    }
}