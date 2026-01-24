package jutjubic.isa.backend.controller;

import jutjubic.isa.backend.dto.CommentDTO;
import jutjubic.isa.backend.dto.video.VideoCardDTO;
import jutjubic.isa.backend.dto.video.VideoDetailsDTO;
import jutjubic.isa.backend.service.PublicVideoService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/{id}/comments")
    public List<CommentDTO> getVideoComments(@PathVariable Long id) {
        return publicVideoService.getCommentsForVideo(id);
    }

    @GetMapping("/{id}")
    public VideoDetailsDTO getVideoDetails(@PathVariable Long id) {

        return publicVideoService.getVideoDetails(id);
    }

//    @GetMapping(value = "/{id}/thumbnail", produces = "image/*")
//    public byte[] getThumbnail(@PathVariable Long id) {
//        return publicVideoService.getThumbnailBytes(id);
//    }

    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long id) {
        return publicVideoService.getThumbnailResponse(id);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> streamVideo(@PathVariable Long id) {
        return publicVideoService.getVideoStreamResponse(id);
    }


}
