package ro.info.iasi.fiipractic.twitter.model;

import jakarta.persistence.*;

@Entity
public class Reply extends Post{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_reply_post_parent",
                    foreignKeyDefinition = "FOREIGN KEY (parent_post_id) REFERENCES posts(id) ON DELETE CASCADE"))
    private Post parentPost;

    private boolean isPublic;

    public Reply() {
    }

    public Reply(User user, String message, long timestamp, Post parentPost, boolean isPublic) {
        super(user, message, timestamp);
        this.parentPost = parentPost;
        this.isPublic = isPublic;
    }

    public Post getParentPost() {
        return parentPost;
    }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        isPublic = isPublic;
    }
}
