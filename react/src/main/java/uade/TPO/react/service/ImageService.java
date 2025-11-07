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

@Service
public class ImageService {

    private static String UPLOAD_DIR = "src/main/resources/static/images/";
    private static final int MAX_IMAGES = 5;

    @Autowired
    private GameService gameService;

    public List<String> uploadImages(Long gameId, List<MultipartFile> files) {
        // Verificar que el juego existe
        Game game = gameService.getById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Juego no encontrado con ID: " + gameId);
        }

        if (files.size() > MAX_IMAGES) {
            throw new IllegalArgumentException("No se pueden subir más de " + MAX_IMAGES + " imágenes.");
        }

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, filename);
                Files.write(filePath, file.getBytes());
                imageUrls.add("/images/" + filename);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir las imágenes: " + e.getMessage());
            }
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