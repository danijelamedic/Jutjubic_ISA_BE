package jutjubic.isa.backend.dto.watchparty;

import java.time.Instant;

public class WatchPartyEventDTO {

    private String type;      // start_video ili error
    private String roomId;
    private Long videoId;
    private String triggeredBy; // email
    private Instant at;
    private String message;     // error

    public WatchPartyEventDTO() {}

    public static WatchPartyEventDTO start(String roomId, Long videoId, String by) {
        WatchPartyEventDTO e = new WatchPartyEventDTO();
        e.type = "START_VIDEO";
        e.roomId = roomId;
        e.videoId = videoId;
        e.triggeredBy = by;
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
