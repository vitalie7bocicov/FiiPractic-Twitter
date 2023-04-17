package ro.info.iasi.fiipractic.twitter.dto.response;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReplyResponseDto that)) return false;
        if (!super.equals(o)) return false;
        return isPublic() == that.isPublic() && Objects.equals(parentPostId, that.parentPostId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parentPostId, isPublic());
    }

        @Override
        public String toString() {
            return "ReplyResponseDto{" +
                    "parentPostId=" + parentPostId +
                    ", isPublic=" + isPublic +
                    ", " + super.toString() +
                    '}';
        }

    }
