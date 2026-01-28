package jutjubic.isa.backend.controller;

import jutjubic.isa.backend.dto.comment.CommentDTO;
import jutjubic.isa.backend.dto.video.VideoCardDTO;
import jutjubic.isa.backend.dto.video.VideoDetailsDTO;
import jutjubic.isa.backend.service.CommentService;
import jutjubic.isa.backend.service.PublicVideoService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/public/videos")
public class PublicVideoController {

    private final PublicVideoService publicVideoService;
    private final CommentService commentService;

    public PublicVideoController(PublicVideoService publicVideoService, CommentService commentService) {
        this.publicVideoService = publicVideoService;
        this.commentService = commentService;
    }

    @GetMapping
    public Page<VideoCardDTO> getPublicVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int safeSize = Math.min(Math.max(size, 1), 50);
        int safePage = Math.max(page, 0);

        return publicVideoService.getPublicVideos(safePage, safeSize);
    }

    @GetMapping("/{id}/comments")
    public Page<CommentDTO> getVideoComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int safeSize = Math.min(Math.max(size, 1), 50);
        int safePage = Math.max(page, 0);

        return commentService.getCommentsForVideo(id, safePage, safeSize);

    }


    @GetMapping("/{id}")
    public VideoDetailsDTO getVideoDetails(@PathVariable Long id) {

        return publicVideoService.getVideoDetails(id);
    }

    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long id) {
        return publicVideoService.getThumbnailResponse(id);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> streamVideo(@PathVariable Long id) {
        return publicVideoService.getVideoStreamResponse(id);
    }

    @PostMapping("/{id}/views")
    public ResponseEntity<Void> incrementViews(@PathVariable Long id) {
        publicVideoService.incrementView(id);
        return ResponseEntity.ok().build();
    }


}
