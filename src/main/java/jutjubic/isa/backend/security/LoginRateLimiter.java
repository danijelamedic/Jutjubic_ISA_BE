package jutjubic.isa.backend.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoginRateLimiter {

    private static final int LIMIT = 5;

    private static class Counter {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile Instant windowStart;
        Counter(Instant start) { this.windowStart = start; }
    }

    private final ConcurrentHashMap<String, Counter> map = new ConcurrentHashMap<>();

    public void checkAllowed(String ip) {
        Instant now = Instant.now();
        Instant window = now.truncatedTo(ChronoUnit.MINUTES);

        Counter c = map.compute(ip, (k, existing) -> {
            if (existing == null) return new Counter(window);
            if (!existing.windowStart.equals(window)) {
                existing.windowStart = window;
                existing.count.set(0);
            }
            return existing;
        });

        int newVal = c.count.incrementAndGet();
        if (newVal > LIMIT) {
            throw new IllegalArgumentException("Previše pokušaja prijave. Pokušajte ponovo za minut.");
        }
    }
}
