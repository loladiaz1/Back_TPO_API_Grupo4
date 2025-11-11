package uade.TPO.react.dto;

import java.time.LocalDateTime;

public class CommunityCommentDTO {

    private Long id;
    private Long postId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;

    public CommunityCommentDTO() {
    }

    public CommunityCommentDTO(Long id, Long postId, String userName, String content, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.userName = userName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}


