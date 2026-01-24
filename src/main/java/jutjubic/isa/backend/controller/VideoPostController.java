package jutjubic.isa.backend.controller;

import jakarta.validation.Valid;
import jutjubic.isa.backend.dto.video.CreateVideoPostRequestDTO;
import jutjubic.isa.backend.dto.video.CreateVideoPostResponseDTO;
import jutjubic.isa.backend.service.VideoPostService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/videos")
public class VideoPostController {

    private final VideoPostService videoPostService;

    public VideoPostController(VideoPostService videoPostService) {
        this.videoPostService = videoPostService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CreateVideoPostResponseDTO createVideoPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("data") String dataJson,
            @RequestPart("video") MultipartFile video,
            @RequestPart("thumbnail") MultipartFile thumbnail
    ) throws Exception {

        CreateVideoPostRequestDTO data =
                new com.fasterxml.jackson.databind.ObjectMapper()
                        .readValue(dataJson, CreateVideoPostRequestDTO.class);

        return videoPostService.createVideoPost(
                userDetails.getUsername(),
                data,
                video,
                thumbnail
        );
    }

}
