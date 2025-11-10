package uade.TPO.react.dto;

public class GameTypeDTO {

    private Long id;
    private String type;

    public GameTypeDTO() {
    }

    public GameTypeDTO(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


