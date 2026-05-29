package exceptionPackage;

public class LineOrderException extends Exception {

    public LineOrderException(String message) {
        super(message);
    }

    public LineOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}