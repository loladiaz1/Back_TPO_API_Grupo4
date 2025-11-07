package uade.TPO.react.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import uade.TPO.react.entity.Game;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    // Variable temporal para las imágenes de prueba
    private Path tempDir;

    @Mock
    private GameService gameService;

    @InjectMocks
    private ImageService imageService;

    private Game game;

    @BeforeEach
    void setUp() {
        try {
            // Crear carpeta temporal (por ejemplo: target/test-images-xxxx)
            tempDir = Files.createTempDirectory("test-images-");

            // Inyectar la carpeta temporal en el campo estático UPLOAD_DIR del ImageService
            try {
                Field uploadDirField = ImageService.class.getDeclaredField("UPLOAD_DIR");
                uploadDirField.setAccessible(true);
                //uploadDirField.set(null, tempDir); // null porque el campo es estático
                uploadDirField.set(null, tempDir.toString());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Error configurando directorio temporal en ImageService", e);
            }

            game = new Game();
            game.setId(42L);
            game.setName("Test Game");

        } catch (IOException e) {
            throw new RuntimeException("Error creando directorio temporal", e);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // Borrar los archivos creados
        if (tempDir != null && Files.exists(tempDir)) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(tempDir)) {
                for (Path p : ds) {
                    Files.deleteIfExists(p);
                }
            }
            Files.deleteIfExists(tempDir);
        }
    }

    @Test
    void testUploadImagesSuccess() throws Exception {
        when(gameService.getById(42L)).thenReturn(game);

        MultipartFile f1 = mock(MultipartFile.class);
        when(f1.getOriginalFilename()).thenReturn("img1.png");
        when(f1.getBytes()).thenReturn("content1".getBytes());

        MultipartFile f2 = mock(MultipartFile.class);
        when(f2.getOriginalFilename()).thenReturn("img2.jpg");
        when(f2.getBytes()).thenReturn("content2".getBytes());

        List<MultipartFile> files = List.of(f1, f2);

        List<String> urls = imageService.uploadImages(42L, files);

        assertEquals(2, urls.size());
        assertTrue(urls.get(0).contains("/images/"));

        // Verificar que los archivos existen en el directorio temporal
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(tempDir)) {
            int count = 0;
            for (Path p : ds) count++;
            assertEquals(2, count);
        }

        verify(gameService).getById(42L);
        verify(gameService).save(game);
    }

    @Test
    void testUploadImagesTooMany() throws Exception {
        List<MultipartFile> files = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            files.add(mock(MultipartFile.class));
        }

        assertThrows(IllegalArgumentException.class, () ->
                imageService.uploadImages(42L, files)
        );
    }

    @Test
    void testUploadImagesGameNotFound() throws Exception {
        when(gameService.getById(999L)).thenReturn(null);
        MultipartFile f1 = mock(MultipartFile.class);

        assertThrows(IllegalArgumentException.class, () ->
                imageService.uploadImages(999L, List.of(f1))
        );
    }

    @Test
    void testUploadImagesIOException() throws Exception {
        when(gameService.getById(42L)).thenReturn(game);

        MultipartFile f1 = mock(MultipartFile.class);
        when(f1.getOriginalFilename()).thenReturn("bad.png");
        when(f1.getBytes()).thenThrow(new IOException("disk error"));

        assertThrows(RuntimeException.class, () ->
                imageService.uploadImages(42L, List.of(f1))
        );
    }
}
