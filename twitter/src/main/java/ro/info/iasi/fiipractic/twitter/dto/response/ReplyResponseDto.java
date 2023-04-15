package ro.info.iasi.fiipractic.twitter.dto.response;

import java.util.UUID;

public class ReplyResponseDto extends PostResponseDto {

    private final UUID parentPostId;

    private final boolean isPublic;

    public ReplyResponseDto(UUID id, String username, String message, long timestamp, UUID parentPostId, boolean isPublic) {
        super(id, username, message, timestamp);
        this.parentPostId = parentPostId;
        this.isPublic = isPublic;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
