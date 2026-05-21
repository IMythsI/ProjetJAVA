package exceptionPackage;

public class ProductException extends Exception {
    public ProductException(String message, Throwable cause) {
        super(message, cause);
    }
}