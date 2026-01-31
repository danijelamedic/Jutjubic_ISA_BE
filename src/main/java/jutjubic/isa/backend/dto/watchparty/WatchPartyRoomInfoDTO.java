package jutjubic.isa.backend.dto.watchparty;

import java.time.Instant;

public class WatchPartyRoomInfoDTO {

    private String roomId;
    private String ownerEmail;
    private Instant createdAt;

    public WatchPartyRoomInfoDTO() {}

    public WatchPartyRoomInfoDTO(String roomId, String ownerEmail, Instant createdAt) {
        this.roomId = roomId;
        this.ownerEmail = ownerEmail;
        this.createdAt = createdAt;
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
