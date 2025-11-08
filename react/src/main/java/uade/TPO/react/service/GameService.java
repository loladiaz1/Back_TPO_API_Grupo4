package uade.TPO.react.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.Game;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.repository.GameRepository; 

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository; 

    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    public Game getById(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        return gameRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado con ID: " + id));
    }

    public Game save(Game game) {
        if (game == null) {
            throw new ValidationException("El juego no puede ser nulo");
        }
        if (game.getName() == null || game.getName().trim().isEmpty()) {
            throw new ValidationException("El nombre del juego es obligatorio");
        }
        if (game.getCost() != null && game.getCost() < 0) {
            throw new ValidationException("El costo no puede ser negativo");
        }
        return gameRepository.save(game);
    }

    public Game update(Long id, Game game) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (game == null) {
            throw new ValidationException("El juego no puede ser nulo");
        }
        
        Game existing = gameRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado con ID: " + id));
        
        if (game.getName() != null && !game.getName().trim().isEmpty()) {
            existing.setName(game.getName());
        } else if (game.getName() != null && game.getName().trim().isEmpty()) {
            throw new ValidationException("El nombre del juego no puede estar vacío");
        }
        
        if (game.getCost() != null) {
            if (game.getCost() < 0) {
                throw new ValidationException("El costo no puede ser negativo");
            }
            existing.setCost(game.getCost());
        }
        
        if (game.getDescription() != null) {
            existing.setDescription(game.getDescription());
        }
        
        if (game.getImages() != null) {
            existing.setImages(game.getImages());
        }
        
        if (game.getTypes() != null) {
            existing.setTypes(game.getTypes());
        }
        
        return gameRepository.save(existing);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Juego no encontrado con ID: " + id);
        }
        gameRepository.deleteById(id);
    }
}