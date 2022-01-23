package pl.polsl.skarbonka.error.exception;

public class BadRequestException extends RuntimeException {
    private final String reason;

    public BadRequestException(String reason) {
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return reason;
    }
}
