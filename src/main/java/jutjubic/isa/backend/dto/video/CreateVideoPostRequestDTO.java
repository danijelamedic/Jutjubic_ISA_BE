package jutjubic.isa.backend.dto.video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateVideoPostRequestDTO {

    @NotBlank
    @Size(max = 120)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String description;

    // csv string, npr: "food,travel,funny"
    @NotBlank
    @Size(max = 500)
    private String tags;

    @Size(max = 255)
    private String location;

    public CreateVideoPostRequestDTO() {}

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTags() { return tags; }
    public String getLocation() { return location; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setTags(String tags) { this.tags = tags; }
    public void setLocation(String location) { this.location = location; }
}
