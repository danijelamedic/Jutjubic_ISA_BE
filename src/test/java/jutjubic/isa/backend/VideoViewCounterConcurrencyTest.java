package jutjubic.isa.backend;

import jutjubic.isa.backend.model.User;
import jutjubic.isa.backend.model.VideoPost;
import jutjubic.isa.backend.repository.UserRepository;
import jutjubic.isa.backend.repository.VideoPostRepository;
import jutjubic.isa.backend.service.PublicVideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VideoViewCounterConcurrencyTest {

    @Autowired private PublicVideoService publicVideoService;
    @Autowired private VideoPostRepository videoPostRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void shouldIncrementViewsCorrectlyUnderConcurrentAccess() throws Exception {
        User author = userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No users found in DB for test"));

        VideoPost v = new VideoPost();
        v.setTitle("Concurrency test");
        v.setDescription("desc");
        v.setTags("a,b");
        v.setVideoPath("uploads/videos/test.mp4");
        v.setThumbnailPath("uploads/thumbnails/test.jpg");
        v.setAuthor(author);
        v.setViewCount(0);

        v = videoPostRepository.saveAndFlush(v);
        Long videoId = v.getId();

        int threads = 50;
        int increments = 200;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch doneGate = new CountDownLatch(increments);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < increments; i++) {
            futures.add(pool.submit(() -> {
                try {
                    startGate.await();
                    publicVideoService.incrementView(videoId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    doneGate.countDown();
                }
            }));
        }

        startGate.countDown();
        assertTrue(doneGate.await(30, TimeUnit.SECONDS));

        for (Future<?> f : futures) {
            f.get();
        }

        pool.shutdown();

        VideoPost after = videoPostRepository.findById(videoId).orElseThrow();
        assertEquals(increments, after.getViewCount());
    }
}
