package uade.TPO.react.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.dto.GameTypeDTO;
import uade.TPO.react.entity.GameType;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.exception.ConflictException;
import uade.TPO.react.repository.GameTypeRepository;

@Service
public class GameTypeService {

    @Autowired
    private GameTypeRepository gameTypeRepository;

    public List<GameTypeDTO> getAllTypes() {
        return gameTypeRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public GameTypeDTO getTypeById(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        GameType type = gameTypeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tipo de juego no encontrado con ID: " + id));
        return toDto(type);
    }

    public GameTypeDTO saveType(GameTypeDTO typeDto) {
        if (typeDto == null) {
            throw new ValidationException("El tipo de juego no puede ser nulo");
        }
        if (typeDto.getType() == null || typeDto.getType().trim().isEmpty()) {
            throw new ValidationException("El nombre del tipo de juego es obligatorio");
        }
        
        // Verificar si ya existe un tipo con el mismo nombre (ignorando mayúsculas/minúsculas)
        List<GameType> existingTypes = gameTypeRepository.findAll();
        for (GameType existing : existingTypes) {
            if (existing.getType() != null && 
                existing.getType().equalsIgnoreCase(typeDto.getType()) &&
                (typeDto.getId() == null || !existing.getId().equals(typeDto.getId()))) {
                throw new ConflictException("Ya existe un tipo de juego con el nombre: " + typeDto.getType());
            }
        }
        
        GameType type = new GameType();
        type.setType(typeDto.getType());
        
        GameType saved = gameTypeRepository.save(type);
        return toDto(saved);
    }

    public GameTypeDTO updateType(Long id, GameTypeDTO typeDto) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (typeDto == null) {
            throw new ValidationException("El tipo de juego no puede ser nulo");
        }
        
        GameType existing = gameTypeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tipo de juego no encontrado con ID: " + id));
        
        if (typeDto.getType() != null && !typeDto.getType().trim().isEmpty()) {
            // Verificar si ya existe otro tipo con el mismo nombre
            List<GameType> existingTypes = gameTypeRepository.findAll();
            for (GameType other : existingTypes) {
                if (other.getType() != null && 
                    other.getType().equalsIgnoreCase(typeDto.getType()) &&
                    !other.getId().equals(id)) {
                    throw new ConflictException("Ya existe un tipo de juego con el nombre: " + typeDto.getType());
                }
            }
            existing.setType(typeDto.getType());
        } else if (typeDto.getType() != null && typeDto.getType().trim().isEmpty()) {
            throw new ValidationException("El nombre del tipo de juego no puede estar vacío");
        }
        
        GameType saved = gameTypeRepository.save(existing);
        return toDto(saved);
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

    private GameTypeDTO toDto(GameType type) {
        if (type == null) {
            return null;
        }
        return new GameTypeDTO(type.getId(), type.getType());
    }
}