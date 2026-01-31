package jutjubic.isa.backend.controller;

import jutjubic.isa.backend.dto.watchparty.CreateWatchPartyRoomResponseDTO;
import jutjubic.isa.backend.dto.watchparty.WatchPartyRoomInfoDTO;
import jutjubic.isa.backend.service.watchparty.WatchPartyRoomService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/watch-party/rooms")
public class WatchPartyController {

    private final WatchPartyRoomService roomService;

    public WatchPartyController(WatchPartyRoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public CreateWatchPartyRoomResponseDTO createRoom(Principal principal) {
        String ownerEmail = principal.getName();

        WatchPartyRoomInfoDTO room = roomService.createRoom(ownerEmail);

        String joinUrl = "/watch-party/" + room.getRoomId();

        return new CreateWatchPartyRoomResponseDTO(room.getRoomId(), room.getOwnerEmail(), joinUrl);
    }

    @GetMapping
    public List<WatchPartyRoomInfoDTO> listRooms() {
        return roomService.listRooms();
    }

    @GetMapping("/{roomId}")
    public WatchPartyRoomInfoDTO getRoom(@PathVariable String roomId) {
        return roomService.getRoom(roomId);
    }
}
