package jutjubic.isa.backend.messaging.listener;

import jutjubic.isa.backend.messaging.dto.UploadCreatedEventDTO;
import jutjubic.isa.backend.messaging.event.VideoUploadedDomainEvent;
import jutjubic.isa.backend.messaging.publisher.UploadEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.time.Instant;
import java.time.ZoneId;

@Component
public class VideoUploadedEventListener {

    private final UploadEventPublisher uploadEventPublisher;

    public VideoUploadedEventListener(UploadEventPublisher uploadEventPublisher) {
        this.uploadEventPublisher = uploadEventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleVideoUploaded(VideoUploadedDomainEvent event) {

        // ---- JSON event ----
        UploadCreatedEventDTO jsonEvent = new UploadCreatedEventDTO();
        jsonEvent.setVideoId(event.getVideoId());
        jsonEvent.setTitle(event.getTitle());
        jsonEvent.setVideoSizeBytes(event.getVideoSizeBytes());
        jsonEvent.setAuthorUsername(event.getAuthorUsername());
        Instant createdAtInstant = event.getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .toInstant();

        jsonEvent.setCreatedAt(createdAtInstant);


        uploadEventPublisher.publishUploadCreatedJson(jsonEvent);

        // ---- Protobuf event ----
        jutjubic.upload.proto.UploadCreatedEvent pbEvent =
                jutjubic.upload.proto.UploadCreatedEvent.newBuilder()
                        .setVideoId(event.getVideoId())
                        .setTitle(event.getTitle())
                        .setVideoSizeBytes(event.getVideoSizeBytes())
                        .setAuthorUsername(event.getAuthorUsername())
                        .setCreatedAtEpochMillis(createdAtInstant.toEpochMilli())
                        .build();

        uploadEventPublisher.publishUploadCreatedPb(pbEvent);
    }
}