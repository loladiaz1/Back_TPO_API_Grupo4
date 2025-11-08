package uade.TPO.react.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.GameType;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.exception.ConflictException;
import uade.TPO.react.repository.GameTypeRepository;

@Service
public class GameTypeService {

    @Autowired
    private GameTypeRepository gameTypeRepository;

    public List<GameType> getAllTypes() {
        return gameTypeRepository.findAll();
    }

    public GameType getTypeById(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        return gameTypeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tipo de juego no encontrado con ID: " + id));
    }

    public GameType saveType(GameType type) {
        if (type == null) {
            throw new ValidationException("El tipo de juego no puede ser nulo");
        }
        if (type.getType() == null || type.getType().trim().isEmpty()) {
            throw new ValidationException("El nombre del tipo de juego es obligatorio");
        }
        
        // Verificar si ya existe un tipo con el mismo nombre (ignorando mayúsculas/minúsculas)
        List<GameType> existingTypes = gameTypeRepository.findAll();
        for (GameType existing : existingTypes) {
            if (existing.getType() != null && 
                existing.getType().equalsIgnoreCase(type.getType()) &&
                (type.getId() == null || !existing.getId().equals(type.getId()))) {
                throw new ConflictException("Ya existe un tipo de juego con el nombre: " + type.getType());
            }
        }
        
        return gameTypeRepository.save(type);
    }

    public GameType updateType(Long id, GameType type) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (type == null) {
            throw new ValidationException("El tipo de juego no puede ser nulo");
        }
        
        GameType existing = gameTypeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tipo de juego no encontrado con ID: " + id));
        
        if (type.getType() != null && !type.getType().trim().isEmpty()) {
            // Verificar si ya existe otro tipo con el mismo nombre
            List<GameType> existingTypes = gameTypeRepository.findAll();
            for (GameType other : existingTypes) {
                if (other.getType() != null && 
                    other.getType().equalsIgnoreCase(type.getType()) &&
                    !other.getId().equals(id)) {
                    throw new ConflictException("Ya existe un tipo de juego con el nombre: " + type.getType());
                }
            }
            existing.setType(type.getType());
        } else if (type.getType() != null && type.getType().trim().isEmpty()) {
            throw new ValidationException("El nombre del tipo de juego no puede estar vacío");
        }
        
        return gameTypeRepository.save(existing);
    }

    public void deleteType(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (!gameTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de juego no encontrado con ID: " + id);
        }
        gameTypeRepository.deleteById(id);
    }
}