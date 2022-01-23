package pl.polsl.skarbonka.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.polsl.skarbonka.error.exception.BadRequestException;
import pl.polsl.skarbonka.error.exception.NotFoundException;
import pl.polsl.skarbonka.error.exception.PermissionDeniedException;
import pl.polsl.skarbonka.error.exception.UnauthorizedException;
import pl.polsl.skarbonka.response.MessageResponse;

@ControllerAdvice
public class HttpExceptionsHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public MessageResponse handleBadRequestException(BadRequestException ex) {
        return new MessageResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseBody
    public MessageResponse handlePermissionDeniedException(PermissionDeniedException ex) {
        return new MessageResponse("You are not allowed to perform this action. Role: "+ ex.getRole().getName());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public MessageResponse handleUnauthorizedException() {
        return new MessageResponse("Unauthorized");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public MessageResponse handleNotFoundException() {
        return new MessageResponse("Resource not found");
    }
}
