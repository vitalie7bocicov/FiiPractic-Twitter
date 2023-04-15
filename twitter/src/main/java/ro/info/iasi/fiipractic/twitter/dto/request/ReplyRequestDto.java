package ro.info.iasi.fiipractic.twitter.dto.request;

import java.util.UUID;

public class ReplyRequestDto extends PostRequestDto{

    private final UUID parentPostId;

    private final boolean isPublic;

    public ReplyRequestDto(String username, String message, UUID parentPostId, boolean isPublic) {
        super(username, message);
        this.parentPostId = parentPostId;
        this.isPublic = isPublic;
    }

    public UUID getParentPostId() {
        return parentPostId;
    }

    public boolean getIsPublic() {
        return isPublic;
    }
}
