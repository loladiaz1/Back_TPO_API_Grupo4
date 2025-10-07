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
}