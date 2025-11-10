package uade.TPO.react.dto;

import java.time.LocalDateTime;

public class CommentDTO {

    private Long id;
    private Long gameId;
    private String userName;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;

    public CommentDTO() {
    }

    public CommentDTO(Long id, Long gameId, String userName, String content, Integer rating, LocalDateTime createdAt) {
        this.id = id;
        this.gameId = gameId;
        this.userName = userName;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}


