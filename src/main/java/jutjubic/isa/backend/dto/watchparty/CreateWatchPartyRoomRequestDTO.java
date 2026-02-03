package jutjubic.isa.backend.dto.watchparty;

import jakarta.validation.constraints.NotNull;

public class CreateWatchPartyRoomRequestDTO {

    @NotNull
    private Long videoId;

    public Long getVideoId() { return videoId; }
    public void setVideoId(Long videoId) { this.videoId = videoId; }
}
