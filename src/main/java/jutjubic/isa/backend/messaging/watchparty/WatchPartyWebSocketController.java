package jutjubic.isa.backend.messaging.watchparty;

import jutjubic.isa.backend.dto.watchparty.StartVideoMessageDTO;
import jutjubic.isa.backend.dto.watchparty.WatchPartyEventDTO;
import jutjubic.isa.backend.dto.watchparty.WatchPartyRoomInfoDTO;
import jutjubic.isa.backend.service.watchparty.WatchPartyRoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WatchPartyWebSocketController {

    private final WatchPartyRoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;

    public WatchPartyWebSocketController(WatchPartyRoomService roomService,
                                  SimpMessagingTemplate messagingTemplate) {
        this.roomService = roomService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/watchparty/{roomId}/join")
    public void join(@DestinationVariable String roomId, Principal principal) {

        if (principal == null) {
            messagingTemplate.convertAndSend(
                    "/topic/watchparty/" + roomId,
                    WatchPartyEventDTO.error(roomId, "principal is null (WS not authenticated)")
            );
            return;
        }

        String email = principal.getName();

        WatchPartyRoomInfoDTO room = roomService.joinRoom(roomId, email);

        messagingTemplate.convertAndSend(
                "/topic/watchparty/" + roomId,
                WatchPartyEventDTO.userJoined(roomId, email, room.getMemberCount())
        );
    }


    @MessageMapping("/watchparty/{roomId}/start")
    public void startVideo(@DestinationVariable String roomId,
                           StartVideoMessageDTO msg,
                           Principal principal) {

        roomService.getRoom(roomId);

        if (principal == null) {
            messagingTemplate.convertAndSend(
                    "/topic/watchparty/" + roomId,
                    WatchPartyEventDTO.error(roomId, "principal is null (WS not authenticated)")
            );
            return;
        }

        String email = principal.getName();


        if (!roomService.isOwner(roomId, email)) {
            messagingTemplate.convertAndSend(
                    "/topic/watchparty/" + roomId,
                    WatchPartyEventDTO.error(roomId, "Only room owner can start video")
            );
            return;
        }

        if (msg == null || msg.getVideoId() == null) {
            messagingTemplate.convertAndSend(
                    "/topic/watchparty/" + roomId,
                    WatchPartyEventDTO.error(roomId, "videoId is required")
            );
            return;
        }

        roomService.startVideo(roomId, msg.getVideoId());

        // broadcast event svima u sobi
        messagingTemplate.convertAndSend(
                "/topic/watchparty/" + roomId,
                WatchPartyEventDTO.start(roomId, msg.getVideoId(), email)
        );
    }
}
