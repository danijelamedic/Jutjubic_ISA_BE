package jutjubic.isa.backend.controller;

import jutjubic.isa.backend.dto.PublicUserDTO;
import jutjubic.isa.backend.service.PublicUserService;
import org.springframework.web.bind.annotation.*;
import jutjubic.isa.backend.dto.VideoCardDTO;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/public/users")
public class PublicUserController {

    private final PublicUserService publicUserService;

    public PublicUserController(PublicUserService publicUserService) {
        this.publicUserService = publicUserService;
    }

    @GetMapping("/{username}")
    public PublicUserDTO getPublicUser(@PathVariable String username) {
        return publicUserService.getPublicUser(username);
    }

    @GetMapping("/{username}/videos")
    public Page<VideoCardDTO> getUserVideos(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return publicUserService.getPublicVideosByUser(username, page, size);
    }

}
