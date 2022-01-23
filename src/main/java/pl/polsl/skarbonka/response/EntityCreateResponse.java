package pl.polsl.skarbonka.response;

import pl.polsl.skarbonka.model.Comment;
import pl.polsl.skarbonka.model.Donation;

import java.util.Date;

public class EntityCreateResponse {

    private final Long entityId;
    private final Date timestamp;

    private EntityCreateResponse(Long entityId, Date timestamp) {
        this.entityId = entityId;
        this.timestamp = timestamp;
    }

    public Long getEntityId() {
        return entityId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public static EntityCreateResponse of(Comment comment, Date timestamp) {
        return new EntityCreateResponse(
                comment.getId(),
                timestamp
        );
    }

    public static EntityCreateResponse of(Donation comment, Date timestamp) {
        return new EntityCreateResponse(
                comment.getId(),
                timestamp
        );
    }
}
