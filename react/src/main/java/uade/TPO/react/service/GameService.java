package uade.TPO.react.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.dto.CommentDTO;
import uade.TPO.react.dto.GameDTO;
import uade.TPO.react.dto.GameTypeDTO;
import uade.TPO.react.entity.Comment;
import uade.TPO.react.entity.Game;
import uade.TPO.react.entity.GameType;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.repository.GameRepository;
import uade.TPO.react.repository.GameTypeRepository; 

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository; 

    @Autowired
    private GameTypeRepository gameTypeRepository;

    public List<GameDTO> getAll() {
        return gameRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public GameDTO getById(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        Game game = gameRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado con ID: " + id));
        return toDto(game);
    }

    public GameDTO save(GameDTO gameDto) {
        if (gameDto == null) {
            throw new ValidationException("El juego no puede ser nulo");
        }
        if (gameDto.getName() == null || gameDto.getName().trim().isEmpty()) {
            throw new ValidationException("El nombre del juego es obligatorio");
        }
        if (gameDto.getCost() != null && gameDto.getCost() < 0) {
            throw new ValidationException("El costo no puede ser negativo");
        }

        Game game = new Game();
        applyDtoToEntity(gameDto, game);

        Game saved = gameRepository.save(game);
        return toDto(saved);
    }

    public GameDTO update(Long id, GameDTO gameDto) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (gameDto == null) {
            throw new ValidationException("El juego no puede ser nulo");
        }
        
        Game existing = gameRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado con ID: " + id));
        
        if (gameDto.getName() != null && !gameDto.getName().trim().isEmpty()) {
            existing.setName(gameDto.getName());
        } else if (gameDto.getName() != null && gameDto.getName().trim().isEmpty()) {
            throw new ValidationException("El nombre del juego no puede estar vacío");
        }
        
        if (gameDto.getCost() != null) {
            if (gameDto.getCost() < 0) {
                throw new ValidationException("El costo no puede ser negativo");
            }
            existing.setCost(gameDto.getCost());
        }
        
        if (gameDto.getDescription() != null) {
            existing.setDescription(gameDto.getDescription());
        }
        
        if (gameDto.getImages() != null) {
            existing.setImages(gameDto.getImages());
        }
        
        if (gameDto.getTypes() != null) {
            existing.setTypes(mapTypes(gameDto.getTypes()));
        }
        
        Game saved = gameRepository.save(existing);
        return toDto(saved);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        
        Game game = gameRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado con ID: " + id));
        
        // Limpiar relaciones ManyToMany manualmente para asegurar eliminación
        if (game.getTypes() != null) {
            game.getTypes().clear();
        }
        
        // Guardar cambios antes de eliminar
        gameRepository.save(game);
        
        // Ahora eliminar el juego (los comentarios e imágenes se eliminan por cascade)
        gameRepository.deleteById(id);
    }

    public GameDTO updateImages(Long gameId, List<String> images) {
        if (gameId == null) {
            throw new ValidationException("El ID del juego no puede ser nulo");
        }
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado con ID: " + gameId));
        game.setImages(images);
        Game saved = gameRepository.save(game);
        return toDto(saved);
    }

    private void applyDtoToEntity(GameDTO dto, Game entity) {
        entity.setName(dto.getName());
        entity.setCost(dto.getCost());
        entity.setDescription(dto.getDescription());
        entity.setImages(dto.getImages());
        if (dto.getTypes() != null) {
            entity.setTypes(mapTypes(dto.getTypes()));
        }
    }

    private List<GameType> mapTypes(List<GameTypeDTO> typeDtos) {
        return typeDtos.stream()
            .map(this::mapType)
            .collect(Collectors.toList());
    }

    private GameType mapType(GameTypeDTO dto) {
        if (dto.getId() == null) {
            throw new ValidationException("El tipo de juego debe incluir un ID");
        }
        return gameTypeRepository.findById(dto.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Tipo de juego no encontrado con ID: " + dto.getId()));
    }

    private GameDTO toDto(Game game) {
        if (game == null) {
            return null;
        }
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setCost(game.getCost());
        dto.setDescription(game.getDescription());
        dto.setImages(game.getImages());
        if (game.getTypes() != null) {
            dto.setTypes(game.getTypes().stream()
                .map(this::toGameTypeDto)
                .collect(Collectors.toList()));
        }
        if (game.getComments() != null) {
            dto.setComments(game.getComments().stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList()));
        }
        return dto;
    }

    private GameTypeDTO toGameTypeDto(GameType type) {
        if (type == null) {
            return null;
        }
        return new GameTypeDTO(type.getId(), type.getType());
    }

    private CommentDTO toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setGameId(comment.getGame() != null ? comment.getGame().getId() : null);
        dto.setUserName(comment.getUserName());
        dto.setContent(comment.getContent());
        dto.setRating(comment.getRating());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}