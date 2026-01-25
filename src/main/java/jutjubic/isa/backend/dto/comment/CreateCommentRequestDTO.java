package jutjubic.isa.backend.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCommentRequestDTO {

    @NotBlank(message = "Comment text is required")
    @Size(max = 1500, message = "Comment text too long")
    private String text;

    public CreateCommentRequestDTO() {}

    public CreateCommentRequestDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
