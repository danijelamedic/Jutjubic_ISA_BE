package jutjubic.isa.backend.dto.watchparty;

import java.time.Instant;

public class WatchPartyRoomInfoDTO {

    private String roomId;
    private String ownerEmail;
    private Instant createdAt;
    private int memberCount;
    private Long currentVideoId;
    private String status;

    public WatchPartyRoomInfoDTO() {}

    public WatchPartyRoomInfoDTO(String roomId, String ownerEmail, Instant createdAt, int memberCount, Long currentVideoId, String status) {
        this.roomId = roomId;
        this.ownerEmail = ownerEmail;
        this.createdAt = createdAt;
        this.memberCount = memberCount;
        this.currentVideoId = currentVideoId;
        this.status = status;
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }

    public Long getCurrentVideoId() { return currentVideoId; }
    public void setCurrentVideoId(Long currentVideoId) { this.currentVideoId = currentVideoId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
