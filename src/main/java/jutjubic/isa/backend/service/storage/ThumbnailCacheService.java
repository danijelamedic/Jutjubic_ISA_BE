package jutjubic.isa.backend.service.storage;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ThumbnailCacheService {

    @Cacheable(value = "thumbnails", key = "#thumbnailPath")
    public byte[] getThumbnailBytes(String thumbnailPath) {
        try {
            return Files.readAllBytes(Paths.get(thumbnailPath));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read thumbnail", e);
        }
    }

    @CacheEvict(value = "thumbnails", key = "#thumbnailPath")
    public void evict(String thumbnailPath) {
        // no-op, annotation does the work
    }
}
