package jutjubic.isa.backend.controller;

import jutjubic.isa.backend.dto.VideoCardDTO;
import jutjubic.isa.backend.service.PublicVideoService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/videos")
public class PublicVideoController {

    private final PublicVideoService publicVideoService;

    public PublicVideoController(PublicVideoService publicVideoService) {
        this.publicVideoService = publicVideoService;
    }

    @GetMapping
    public Page<VideoCardDTO> getPublicVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return publicVideoService.getPublicVideos(page, size);
    }
}
