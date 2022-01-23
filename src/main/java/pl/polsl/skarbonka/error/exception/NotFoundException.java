package pl.polsl.skarbonka.error.exception;

public class NotFoundException extends RuntimeException {
    private final String reason;
    public NotFoundException() {
        this.reason = "Resource not found";
    }
    public NotFoundException(String reason) {
        this.reason = reason;
    }
    @Override
    public String getMessage() {
        return reason;
    }
}
