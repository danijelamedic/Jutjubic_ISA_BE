package jutjubic.isa.backend.controller;

import jakarta.validation.Valid;
import jutjubic.isa.backend.dto.comment.CommentDTO;
import jutjubic.isa.backend.dto.comment.CreateCommentRequestDTO;
import jutjubic.isa.backend.model.User;
import jutjubic.isa.backend.service.CommentService;
import jutjubic.isa.backend.service.CustomUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
public class CommentController {

    private final CommentService commentService;
    private final CustomUserDetailsService customUserDetailsService;

    public CommentController(CommentService commentService, CustomUserDetailsService customUserDetailsService) {
        this.commentService = commentService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/{videoId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO createComment(
            @PathVariable Long videoId,
            @Valid @RequestBody CreateCommentRequestDTO req
    ) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = customUserDetailsService.getDomainUserByEmail(email);

        return commentService.createComment(videoId, req.getText(), user);
    }
}
