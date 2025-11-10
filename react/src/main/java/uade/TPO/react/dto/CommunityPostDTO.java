package uade.TPO.react.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommunityPostDTO {

    private Long id;
    private String authorName;
    private String title;
    private String content;
    private String category;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Integer likes;
    private List<CommunityCommentDTO> comments = new ArrayList<>();

    public CommunityPostDTO() {
    }

    public CommunityPostDTO(Long id, String authorName, String title, String content, String category,
            String imageUrl, LocalDateTime createdAt, Integer likes, List<CommunityCommentDTO> comments) {
        this.id = id;
        this.authorName = authorName;
        this.title = title;
        this.content = content;
        this.category = category;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.likes = likes;
        if (comments != null) {
            this.comments = comments;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public List<CommunityCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommunityCommentDTO> comments) {
        this.comments = comments != null ? comments : new ArrayList<>();
    }
}


