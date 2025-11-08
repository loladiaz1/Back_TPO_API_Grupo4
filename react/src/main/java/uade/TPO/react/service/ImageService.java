package uade.TPO.react.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import uade.TPO.react.entity.Game;
import uade.TPO.react.exception.ValidationException;

@Service
public class ImageService {

    private static String UPLOAD_DIR = "src/main/resources/static/images/";
    private static final int MAX_IMAGES = 5;

    @Autowired
    private GameService gameService;

    public List<String> uploadImages(Long gameId, List<MultipartFile> files) {
        if (gameId == null) {
            throw new ValidationException("El ID del juego no puede ser nulo");
        }
        if (files == null || files.isEmpty()) {
            throw new ValidationException("Debe proporcionar al menos una imagen");
        }

        // Verificar que el juego existe (getById ya lanza ResourceNotFoundException si no existe)
        Game game = gameService.getById(gameId);

        if (files.size() > MAX_IMAGES) {
            throw new ValidationException("No se pueden subir más de " + MAX_IMAGES + " imágenes");
        }

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new ValidationException("No se pueden subir archivos vacíos");
            }
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            
            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new ValidationException("Error al crear el directorio de imágenes: " + e.getMessage());
                }
            }
            
            // Escribir el archivo - IOException se propagará y será manejada por GlobalExceptionHandler
            try {
                Files.write(filePath, file.getBytes());
            } catch (IOException e) {
                throw new ValidationException("Error al subir la imagen " + file.getOriginalFilename() + ": " + e.getMessage());
            }
            imageUrls.add("/images/" + filename);
        }

        // Agregar las imágenes al juego
        if (game.getImages() == null) {
            game.setImages(new ArrayList<>());
        }
        game.getImages().addAll(imageUrls);

        // Guardar el juego actualizado
        gameService.save(game);

        return imageUrls;
    }
}