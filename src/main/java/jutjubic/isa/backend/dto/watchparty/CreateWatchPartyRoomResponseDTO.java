package jutjubic.isa.backend.dto.watchparty;

public class CreateWatchPartyRoomResponseDTO {

    private String roomId;
    private String ownerEmail;
    private String joinUrl;

    public CreateWatchPartyRoomResponseDTO() {}

    public CreateWatchPartyRoomResponseDTO(String roomId, String ownerEmail, String joinUrl) {
        this.roomId = roomId;
        this.ownerEmail = ownerEmail;
        this.joinUrl = joinUrl;
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public String getJoinUrl() { return joinUrl; }
    public void setJoinUrl(String joinUrl) { this.joinUrl = joinUrl; }
}
