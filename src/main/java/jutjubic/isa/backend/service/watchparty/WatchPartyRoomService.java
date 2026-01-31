package jutjubic.isa.backend.service.watchparty;

import jutjubic.isa.backend.dto.watchparty.WatchPartyRoomInfoDTO;
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

        private RoomState(String roomId, String ownerEmail, Instant createdAt) {
            this.roomId = roomId;
            this.ownerEmail = ownerEmail;
            this.createdAt = createdAt;
        }
    }

    private final Map<String, RoomState> rooms = new ConcurrentHashMap<>();

    public WatchPartyRoomInfoDTO createRoom(String ownerEmail) {
        String roomId = UUID.randomUUID().toString();
        RoomState state = new RoomState(roomId, ownerEmail, Instant.now());
        rooms.put(roomId, state);
        System.out.println("[createRoom] roomId=" + roomId + " roomsSize=" + rooms.size());
        return new WatchPartyRoomInfoDTO(state.roomId, state.ownerEmail, state.createdAt);
    }

    public WatchPartyRoomInfoDTO getRoom(String roomId) {

        System.out.println("[getRoom] roomId='" + roomId + "' len=" + (roomId == null ? -1 : roomId.length())
                + " keys=" + rooms.keySet());

        if (roomId != null) roomId = roomId.trim();
        RoomState state = rooms.get(roomId);
        if (state == null) {
            throw new IllegalArgumentException("Watch party room not found");
        }
        return new WatchPartyRoomInfoDTO(state.roomId, state.ownerEmail, state.createdAt);
    }

    public List<WatchPartyRoomInfoDTO> listRooms() {
        List<WatchPartyRoomInfoDTO> out = new ArrayList<>();
        for (RoomState s : rooms.values()) {
            out.add(new WatchPartyRoomInfoDTO(s.roomId, s.ownerEmail, s.createdAt));
        }
        out.sort((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return out;
    }

    public boolean isOwner(String roomId, String email) {
        if (roomId != null) roomId = roomId.trim();
        RoomState state = rooms.get(roomId);
        if (state == null) return false;
        return state.ownerEmail.equals(email);
    }
}
