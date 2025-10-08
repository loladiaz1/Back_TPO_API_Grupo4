package uade.TPO.react.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double cost;
    //private String image;
    private String description;

    /*
    // Campo para almacenar el promedio de calificaciones (reviews) de los usuarios.
    // Este valor se calculará en base a las reseñas cuando la clase User y Comment estén implementadas.
    private Double averageReview;
    */

    @ManyToMany
    @JoinTable(
        name = "game_game_type",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private List<GameType> types; 

    //@OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Comment> comments;  



    public Game() {
    }

    public Game(String name, Double cost, String image, String description) {
        this.name = name;
        this.cost = cost;
        //this.image = image;
        this.description = description;
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

    /*public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }*/

    /*
    public Double getAverageReview() {
        return averageReview;
    }

    public void setAverageReview(Double averageReview) {
        this.averageReview = averageReview;
    }
    */

    /*public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }*/ 


    public List<GameType> getTypes() {
        return types;
    }

    public void setTypes(List<GameType> types) {
        this.types = types;
    }
    /*
    // Helpers para Comment (OneToMany)
    public void addComment(Comment comment) {
        if (this.comments == null) this.comments = new ArrayList<>();
        this.comments.add(comment);
        comment.setGame(this);
    }

    public void removeComment(Comment comment) {
        if (this.comments != null) {
            this.comments.remove(comment);
            comment.setGame(null);
        }
    }

    // Helpers para GameType (ManyToMany)
    public void addType(GameType type) {
        if (this.types == null) this.types = new ArrayList<>();
        this.types.add(type);
        if (type.getGames() == null) type.setGames(new ArrayList<>());
        type.getGames().add(this);
    }

    public void removeType(GameType type) {
        if (this.types != null) {
            this.types.remove(type);
            if (type.getGames() != null) type.getGames().remove(this);
        }
    }
    */
}