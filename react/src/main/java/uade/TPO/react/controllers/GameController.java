package uade.TPO.react.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uade.TPO.react.entity.Game;
import uade.TPO.react.service.GameService; 


@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    // Obtener todos los juegos
    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAll();
    }

    // Obtener un juego por ID
    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Long id) {
        return gameService.getById(id);
    }

    // Crear un nuevo juego
    @PostMapping
    public Game createGame(@RequestBody Game game) {
        return gameService.save(game);
    }

    // Actualizar un juego existente
    @PutMapping("/{id}")
    public Game updateGame(@PathVariable Long id, @RequestBody Game game) {
        game.setId(id);
        return gameService.save(game);
    }

    // Eliminar un juego por ID
    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.delete(id);
    }
}
