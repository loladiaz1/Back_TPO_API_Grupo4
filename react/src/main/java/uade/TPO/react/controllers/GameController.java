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
import uade.TPO.react.service.GameService; // ← Agregar este import

@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "*") 
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public List<Game> getAll() {
        return gameService.getAll(); // ← Usar instancia, no clase
    }

    @GetMapping("/{id}")
    public Game getById(@PathVariable Long id) {
        return gameService.getById(id); // ← Usar instancia
    }

    @PostMapping
    public Game save(@RequestBody Game game) {
        return gameService.save(game); // ← Usar instancia
    }

    @PutMapping("/{id}")
    public Game update(@PathVariable Long id, @RequestBody Game game) {
        game.setId(id);
        return gameService.save(game); // ← Usar instancia
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        gameService.delete(id); // ← Usar instancia
    }
}