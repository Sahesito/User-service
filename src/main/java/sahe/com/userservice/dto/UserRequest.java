package sahe.com.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import sahe.com.userservice.model.User;

@Data
public class UserRequest {

    @NotBlank(message = "Nombre requerido")
    private String firstName;

    @NotBlank(message = "Apellidos requeridos")
    private String lastName;

    @NotBlank(message = "Correo requerido")
    @Email(message = "Correo debe ser valido")
    private String email;

    private User.Role role = User.Role.CLIENT;

    private Boolean active = true;

    private String phone;

    private String address;

    private String city;

    private String country;
}