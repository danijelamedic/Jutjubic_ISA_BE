package jutjubic.isa.backend.messaging.listener;

import jutjubic.isa.backend.messaging.dto.UploadCreatedEvent;
import jutjubic.isa.backend.messaging.event.VideoUploadedDomainEvent;
import jutjubic.isa.backend.messaging.publisher.UploadEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
public class VideoUploadedEventListener {

    private final UploadEventPublisher uploadEventPublisher;

    public VideoUploadedEventListener(UploadEventPublisher uploadEventPublisher) {
        this.uploadEventPublisher = uploadEventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVideoUploaded(VideoUploadedDomainEvent event) {
        UploadCreatedEvent msg = new UploadCreatedEvent();
        msg.setVideoId(event.getVideoId());
        msg.setTitle(event.getTitle());
        msg.setAuthorUsername(event.getAuthorUsername());
        msg.setVideoSizeBytes(event.getVideoSizeBytes());
        msg.setThumbnailSizeBytes(event.getThumbnailSizeBytes());
        msg.setCreatedAt(event.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant());
        uploadEventPublisher.publishUploadCreated(msg);
    }

}
