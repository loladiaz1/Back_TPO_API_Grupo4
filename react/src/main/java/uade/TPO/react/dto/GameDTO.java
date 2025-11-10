package uade.TPO.react.dto;

import java.util.ArrayList;
import java.util.List;

public class GameDTO {

    private Long id;
    private String name;
    private Double cost;
    private String description;
    private List<String> images = new ArrayList<>();
    private List<GameTypeDTO> types = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();

    public GameDTO() {
    }

    public GameDTO(Long id, String name, Double cost, String description, List<String> images,
            List<GameTypeDTO> types, List<CommentDTO> comments) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.description = description;
        if (images != null) {
            this.images = images;
        }
        if (types != null) {
            this.types = types;
        }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images != null ? images : new ArrayList<>();
    }

    public List<GameTypeDTO> getTypes() {
        return types;
    }

    public void setTypes(List<GameTypeDTO> types) {
        this.types = types != null ? types : new ArrayList<>();
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments != null ? comments : new ArrayList<>();
    }
}


