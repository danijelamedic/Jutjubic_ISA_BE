package jutjubic.isa.backend.dto;

public class PublicUserDTO {

    private String username;
    private String firstName;
    private String lastName;

    public PublicUserDTO() {}

    public PublicUserDTO(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
