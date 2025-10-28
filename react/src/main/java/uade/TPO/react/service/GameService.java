package uade.TPO.react.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.Game;
import uade.TPO.react.repository.GameRepository; 

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository; 

    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    public Game getById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public void delete(Long id) {
        gameRepository.deleteById(id);
    }

    public Game update(Long id, Game game) {
        Game existing = gameRepository.findById(id).orElse(null);
        if (existing == null) {
            throw new RuntimeException("Juego no encontrado con ID: " + id);
        }
        
        // Actualizar campos del juego existente
        if (game.getName() != null) {
            existing.setName(game.getName());
        }
        if (game.getDescription() != null) {
            existing.setDescription(game.getDescription());
        }
        if (game.getCost() != null) {
            existing.setCost(game.getCost());
        }
        if (game.getTypes() != null) {
            existing.setTypes(game.getTypes());
        }
        if (game.getImages() != null) {
            existing.setImages(game.getImages());
        }
        
        return gameRepository.save(existing);
    }
}