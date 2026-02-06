package jutjubic.isa.backend.service.watchparty;

import jutjubic.isa.backend.dto.watchparty.WatchPartyRoomInfoDTO;
import jutjubic.isa.backend.model.watchparty.RoomStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WatchPartyRoomService {


    private static class RoomState {
        private final String roomId;
        private final String ownerEmail;
        private final Instant createdAt;
        private final Set<String> members = ConcurrentHashMap.newKeySet();
        private volatile Long currentVideoId;
        private volatile RoomStatus status = RoomStatus.WAITING;

        private RoomState(String roomId, String ownerEmail, Instant createdAt, Long currentVideoId) {
            this.roomId = roomId;
            this.ownerEmail = ownerEmail;
            this.createdAt = createdAt;
            this.currentVideoId = currentVideoId;
        }
    }

    private final Map<String, RoomState> rooms = new ConcurrentHashMap<>();

    public WatchPartyRoomInfoDTO createRoom(String ownerEmail, Long initialVideoId) {
        String roomId = UUID.randomUUID().toString();
        RoomState state = new RoomState(roomId, ownerEmail, Instant.now(), initialVideoId);

        // owner je automatski clan sobe
        state.members.add(ownerEmail);

        rooms.put(roomId, state);
        return toDto(state);
    }

    public WatchPartyRoomInfoDTO getRoom(String roomId) {
        if (roomId != null) roomId = roomId.trim();
        RoomState state = rooms.get(roomId);
        if (state == null) throw new IllegalArgumentException("Watch party room not found");
        return toDto(state);
    }

    public List<WatchPartyRoomInfoDTO> listRooms() {
        List<WatchPartyRoomInfoDTO> out = new ArrayList<>();
        for (RoomState s : rooms.values()) out.add(toDto(s));
        out.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return out;
    }

    public boolean isOwner(String roomId, String email) {
        if (roomId != null) roomId = roomId.trim();
        RoomState state = rooms.get(roomId);
        return state != null && state.ownerEmail.equals(email);
    }

    public WatchPartyRoomInfoDTO joinRoom(String roomId, String email) {
        if (roomId != null) roomId = roomId.trim();
        RoomState state = rooms.get(roomId);
        if (state == null) throw new IllegalArgumentException("Watch party room not found");

        state.members.add(email);
        return toDto(state);
    }

    public WatchPartyRoomInfoDTO leaveRoom(String roomId, String email) {
        if (roomId != null) roomId = roomId.trim();
        RoomState state = rooms.get(roomId);
        if (state == null) throw new IllegalArgumentException("Watch party room not found");

        state.members.remove(email);

        if (state.ownerEmail.equals(email)) {
            state.status = RoomStatus.CLOSED;
            return toDto(state);
        }

        return toDto(state);
    }


    public WatchPartyRoomInfoDTO startVideo(String roomId, Long videoId) {
        if (roomId != null) roomId = roomId.trim();
        RoomState state = rooms.get(roomId);
        if (state == null) throw new IllegalArgumentException("Watch party room not found");

        state.currentVideoId = videoId;
        state.status = RoomStatus.STARTED;
        return toDto(state);
    }

    private WatchPartyRoomInfoDTO toDto(RoomState s) {
        WatchPartyRoomInfoDTO dto = new WatchPartyRoomInfoDTO();
        dto.setRoomId(s.roomId);
        dto.setOwnerEmail(s.ownerEmail);
        dto.setCreatedAt(s.createdAt);
        dto.setMemberCount(s.members.size());
        dto.setCurrentVideoId(s.currentVideoId);
        dto.setStatus(s.status.name());

        return dto;
    }
}

