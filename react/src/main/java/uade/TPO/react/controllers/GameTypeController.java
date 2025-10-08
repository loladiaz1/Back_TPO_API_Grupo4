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

import uade.TPO.react.entity.GameType;
import uade.TPO.react.service.GameTypeService;

@RestController
@RequestMapping("/gametypes")
@CrossOrigin(origins = "*")
public class GameTypeController {

    @Autowired
    private GameTypeService gameTypeService;

    @GetMapping
    public List<GameType> getAllTypes() {
        return gameTypeService.getAllTypes();
    }

    @GetMapping("/{id}")
    public GameType getTypeById(@PathVariable Long id) {
        return gameTypeService.getTypeById(id);
    }

    @PostMapping
    public GameType createType(@RequestBody GameType type) {
        return gameTypeService.saveType(type);
    }

    @PutMapping("/{id}")
    public GameType updateType(@PathVariable Long id, @RequestBody GameType updatedType) {
        updatedType.setId(id);
        return gameTypeService.saveType(updatedType);
    }

    @DeleteMapping("/{id}")
    public void deleteType(@PathVariable Long id) {
        gameTypeService.deleteType(id);
    }
}
