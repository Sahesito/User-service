package sahe.com.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import sahe.com.userservice.model.User;

@Data
public class UserRequest {

    @NotBlank(message = "Name Required")
    private String firstName;

    @NotBlank(message = "LastNames Required")
    private String lastName;

    @NotBlank(message = "Email Required")
    @Email(message = "Email address must be valid")
    private String email;
    private User.Role role = User.Role.CLIENT;
    private Boolean active = true;
    private String phone;
    private String address;
    private String city;
    private String country;
}