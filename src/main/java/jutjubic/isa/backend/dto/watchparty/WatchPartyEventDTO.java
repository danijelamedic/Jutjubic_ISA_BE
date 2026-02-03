package jutjubic.isa.backend.dto.watchparty;

import java.time.Instant;

public class WatchPartyEventDTO {

    private String type;      // start_video / error / user_joined / room_created / user_left / room_closed
    private String roomId;
    private Long videoId;
    private String triggeredBy; // email
    private Instant at;
    private String message;     // error
    private String ownerEmail;
    private String joinedUser;
    private Integer memberCount;

    public WatchPartyEventDTO() {}

    public static WatchPartyEventDTO start(String roomId, Long videoId, String triggeredBy) {
        WatchPartyEventDTO e = new WatchPartyEventDTO();
        e.type = "START_VIDEO";
        e.roomId = roomId;
        e.videoId = videoId;
        e.triggeredBy = triggeredBy;
        e.at = Instant.now();
        return e;
    }

    public static WatchPartyEventDTO error(String roomId, String msg) {
        WatchPartyEventDTO e = new WatchPartyEventDTO();
        e.type = "ERROR";
        e.roomId = roomId;
        e.message = msg;
        e.at = Instant.now();
        return e;
    }

    public static WatchPartyEventDTO userJoined(String roomId, String joinedUser, int memberCount) {
        WatchPartyEventDTO dto = new WatchPartyEventDTO();
        dto.type = "USER_JOINED";
        dto.roomId = roomId;
        dto.joinedUser = joinedUser;
        dto.memberCount = memberCount;
        dto.at = Instant.now();
        return dto;
    }

    public static WatchPartyEventDTO roomCreated(String roomId, String triggeredBy, int memberCount) {
        WatchPartyEventDTO dto = new WatchPartyEventDTO();
        dto.type = "ROOM_CREATED";
        dto.roomId = roomId;
        dto.triggeredBy = triggeredBy;
        dto.memberCount = memberCount;
        dto.at = Instant.now();
        return dto;
    }

    public static WatchPartyEventDTO userLeft(String roomId, String leftUser, int memberCount) {
        WatchPartyEventDTO dto = new WatchPartyEventDTO();
        dto.type = "USER_LEFT";
        dto.roomId = roomId;
        dto.joinedUser = leftUser;
        dto.memberCount = memberCount;
        dto.at = Instant.now();
        return dto;
    }

    public static WatchPartyEventDTO roomClosed(String roomId, String triggeredBy) {
        WatchPartyEventDTO dto = new WatchPartyEventDTO();
        dto.type = "ROOM_CLOSED";
        dto.roomId = roomId;
        dto.triggeredBy = triggeredBy;
        dto.at = Instant.now();
        return dto;
    }



    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public Long getVideoId() { return videoId; }
    public void setVideoId(Long videoId) { this.videoId = videoId; }

    public String getTriggeredBy() { return triggeredBy; }
    public void setTriggeredBy(String triggeredBy) { this.triggeredBy = triggeredBy; }

    public Instant getAt() { return at; }
    public void setAt(Instant at) { this.at = at; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
