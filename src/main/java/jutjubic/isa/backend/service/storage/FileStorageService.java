package jutjubic.isa.backend.service.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import java.util.concurrent.*;

@Service
public class FileStorageService {

    private static final Path VIDEO_DIR = Paths.get("uploads/videos");
    private static final Path THUMBNAIL_DIR = Paths.get("uploads/thumbnails");

    public FileStorageService() throws IOException {
        Files.createDirectories(VIDEO_DIR);
        Files.createDirectories(THUMBNAIL_DIR);
    }

    public String storeVideo(MultipartFile file) {
        return store(file, VIDEO_DIR);
    }

    public String storeThumbnail(MultipartFile file) {
        return store(file, THUMBNAIL_DIR);
    }

    private static final long UPLOAD_TIMEOUT_SECONDS = 30;

    private String store(MultipartFile file, Path dir) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path target = dir.resolve(filename);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            try {
                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            future.get(UPLOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return target.toString();
        } catch (TimeoutException e) {
            future.cancel(true);
            deleteFile(target.toString());
            throw new RuntimeException("Upload timed out (>" + UPLOAD_TIMEOUT_SECONDS + "s)");
        } catch (ExecutionException e) {
            deleteFile(target.toString());
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException re) throw re;
            throw new RuntimeException("Failed to store file", cause);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            deleteFile(target.toString());
            throw new RuntimeException("Upload interrupted");
        } finally {
            executor.shutdownNow();
        }
    }


    public void deleteFile(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException ignored) {}
    }
}
