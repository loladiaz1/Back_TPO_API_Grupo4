package uade.TPO.react.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import uade.TPO.react.entity.Game;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.service.GameService;

@RestController
@CrossOrigin(origins = "*")
public class ImageController {

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @Autowired
    private GameService gameService;

    @PostMapping("/games/{gameId}/images/upload")
    public ResponseEntity<?> uploadImages(
            @PathVariable Long gameId,
            @RequestParam("files") List<MultipartFile> files) throws IOException {
        
        // Verificar que el juego existe
        Game game = gameService.getById(gameId);
        if (game == null) {
            throw new ResourceNotFoundException("Juego no encontrado con ID: " + gameId);
        }

        if (files.size() > 5) {
            throw new ValidationException("No se pueden subir más de 5 imágenes.");
        }

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            Files.write(filePath, file.getBytes());
            imageUrls.add("/images/" + filename);
        }

        // Agregar las imágenes al juego
        if (game.getImages() == null) {
            game.setImages(new ArrayList<>());
        }
        game.getImages().addAll(imageUrls);
        
        // Guardar el juego actualizado
        gameService.save(game);

        return ResponseEntity.ok(imageUrls);
    }
}