package pl.polsl.skarbonka.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.polsl.skarbonka.response.MessageResponse;

@Component
public class ResponseEntityUtil {
    public ResponseEntity<MessageResponse> successfulMessageResponseEntity(String message) {
        return new ResponseEntity<>(new MessageResponse(message), HttpStatus.ACCEPTED);
    }
}
