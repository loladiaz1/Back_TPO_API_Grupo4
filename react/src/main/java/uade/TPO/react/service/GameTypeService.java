package uade.TPO.react.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.GameType;
import uade.TPO.react.repository.GameTypeRepository;

@Service
public class GameTypeService {

    @Autowired
    private GameTypeRepository gameTypeRepository;

    public List<GameType> getAllTypes() {
        return gameTypeRepository.findAll();
    }

    public GameType getTypeById(Long id) {
        return gameTypeRepository.findById(id).orElse(null);
    }

    public GameType saveType(GameType type) {
        return gameTypeRepository.save(type);
    }

    public void deleteType(Long id) {
        gameTypeRepository.deleteById(id);
    }
}