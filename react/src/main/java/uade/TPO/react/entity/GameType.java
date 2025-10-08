package uade.TPO.react.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class GameType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Ej: "Acción", "Aventura", "RPG", etc.

    // NO incluimos la relación inversa para evitar el loop infinito
    // Game tiene la referencia a GameType, pero GameType no tiene referencia a Game

    // ---------- Constructores ----------
    public GameType() {
    }

    public GameType(String type) {
        this.type = type;
    }

    // ---------- Getters y Setters ----------
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