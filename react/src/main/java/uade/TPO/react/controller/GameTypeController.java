package uade.TPO.react.controller;

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

import uade.TPO.react.dto.GameTypeDTO;
import uade.TPO.react.service.GameTypeService;

@RestController
@RequestMapping("/api/gametypes")
//@CrossOrigin(origins = "*")
public class GameTypeController {

    @Autowired
    private GameTypeService gameTypeService;

    @GetMapping
    public List<GameTypeDTO> getAllTypes() {
        return gameTypeService.getAllTypes();
    }

    @GetMapping("/{id}")
    public GameTypeDTO getTypeById(@PathVariable Long id) {
        return gameTypeService.getTypeById(id);
    }

    @PostMapping
    public GameTypeDTO createType(@RequestBody GameTypeDTO type) {
        return gameTypeService.saveType(type);
    }

    @PutMapping("/{id}")
    public GameTypeDTO updateType(@PathVariable Long id, @RequestBody GameTypeDTO updatedType) {
        return gameTypeService.updateType(id, updatedType);
    }

    @DeleteMapping("/{id}")
    public void deleteType(@PathVariable Long id) {
        gameTypeService.deleteType(id);
    }
}
