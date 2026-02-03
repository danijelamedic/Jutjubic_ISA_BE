package jutjubic.isa.backend.controller;

import jakarta.validation.Valid;
import jutjubic.isa.backend.dto.watchparty.CreateWatchPartyRoomRequestDTO;
import jutjubic.isa.backend.dto.watchparty.CreateWatchPartyRoomResponseDTO;
import jutjubic.isa.backend.dto.watchparty.WatchPartyEventDTO;
import jutjubic.isa.backend.dto.watchparty.WatchPartyRoomInfoDTO;
import jutjubic.isa.backend.service.watchparty.WatchPartyRoomService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/watch-party/rooms")
public class WatchPartyController {

    private final WatchPartyRoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;


    public WatchPartyController(WatchPartyRoomService roomService, SimpMessagingTemplate messagingTemplate) {
        this.roomService = roomService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping
    public CreateWatchPartyRoomResponseDTO createRoom(
            @Valid @RequestBody CreateWatchPartyRoomRequestDTO req,
            Principal principal
    ) {
        String ownerEmail = principal.getName();
        WatchPartyRoomInfoDTO room = roomService.createRoom(ownerEmail, req.getVideoId());

        messagingTemplate.convertAndSend(
                "/topic/watchparty/rooms",
                WatchPartyEventDTO.roomCreated(
                        room.getRoomId(),
                        ownerEmail,
                        room.getMemberCount()
                )
        );
        String joinUrl = "/watch-party/" + room.getRoomId();

        return new CreateWatchPartyRoomResponseDTO(
                room.getRoomId(),
                room.getOwnerEmail(),
                joinUrl
        );
    }

    @GetMapping
    public List<WatchPartyRoomInfoDTO> listRooms() {
        return roomService.listRooms();
    }

    @GetMapping("/{roomId}")
    public WatchPartyRoomInfoDTO getRoom(@PathVariable String roomId) {
        return roomService.getRoom(roomId);
    }

    @PostMapping("/{roomId}/join")
    public WatchPartyRoomInfoDTO joinRoom(@PathVariable String roomId, Principal principal) {
        String email = principal.getName();
        WatchPartyRoomInfoDTO room = roomService.joinRoom(roomId, email);

        messagingTemplate.convertAndSend(
                "/topic/watchparty/rooms",
                WatchPartyEventDTO.userJoined(
                        roomId,
                        email,
                        room.getMemberCount()
                )
        );
        messagingTemplate.convertAndSend(
                "/topic/watchparty/" + roomId,
                WatchPartyEventDTO.userJoined(roomId, email, room.getMemberCount())
        );

        return room;
    }

    @PostMapping("/{roomId}/leave")
    public WatchPartyRoomInfoDTO leaveRoom(@PathVariable String roomId, Principal principal) {
        String email = principal.getName();

        WatchPartyRoomInfoDTO room = roomService.leaveRoom(roomId, email);

        // owner je izasao = soba se obrisala
        if (room == null) {
            // obavesti lobby da je soba zatvorena
            messagingTemplate.convertAndSend(
                    "/topic/watchparty/rooms",
                    WatchPartyEventDTO.roomClosed(roomId, email)
            );

            // obavesti sobu (ako neko još slusa) da je zatvorena
            messagingTemplate.convertAndSend(
                    "/topic/watchparty/" + roomId,
                    WatchPartyEventDTO.roomClosed(roomId, email)
            );

            return null;
        }

        // guest je izasao = obavesti lobby i sobu
        messagingTemplate.convertAndSend(
                "/topic/watchparty/rooms",
                WatchPartyEventDTO.userLeft(roomId, email, room.getMemberCount())
        );

        messagingTemplate.convertAndSend(
                "/topic/watchparty/" + roomId,
                WatchPartyEventDTO.userLeft(roomId, email, room.getMemberCount())
        );

        return room;
    }


}
