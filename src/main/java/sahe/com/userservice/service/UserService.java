package sahe.com.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sahe.com.userservice.dto.UserRequest;
import sahe.com.userservice.dto.UserResponse;
import sahe.com.userservice.model.User;
import sahe.com.userservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* Todos los usuarios */
    public List<UserResponse> getAllUsers() {
        log.info("Extrayendo todos los usuarios");
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    /* Usuario por Id*/
    public UserResponse getUserById(Long id) {
        log.info("Buscando usuario por id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado por id: " + id));
        return new UserResponse(user);
    }

    /* Usuario por correo */
    public UserResponse getUserByEmail(String email) {
        log.info("Buscando usuario por correo: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado por correo: " + email));
        return new UserResponse(user);
    }

    /* Usuario por roles*/
    public List<UserResponse> getUsersByRole(User.Role role) {
        log.info("Buscando usuario por rol: {}", role);
        return userRepository.findByRole(role)
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    /* Usuario por estado*/
    public List<UserResponse> getUsersByActiveStatus(Boolean active) {
        log.info("Buscando usuario por estado: {}", active);
        return userRepository.findByActive(active)
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    /* Crear Usuario*/
    @Transactional
    public UserResponse createUser(UserRequest request) {
        log.info("Creando usuario con el correo: {}", request.getEmail());

        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya existe: " + request.getEmail());
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setActive(request.getActive());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());

        User savedUser = userRepository.save(user);
        log.info("Usuario creado correctamente id: {}", savedUser.getId());

        return new UserResponse(savedUser);
    }

    /* Actualizar Usuario */
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        log.info("Actualizando usuario por id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id no encontrado: " + id));

        // Verificar si el email cambió y ya existe
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Correo existente: " + request.getEmail());
        }

        // Actualizar campos
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setActive(request.getActive());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());

        User updatedUser = userRepository.save(user);
        log.info("Usuario actualizado correctamente con id: {}", updatedUser.getId());

        return new UserResponse(updatedUser);
    }

    /* Eliminar Usuario (Soft delete) */
    @Transactional
    public void deleteUser(Long id) {
        log.info("Borrando usuario con id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // Soft delete: solo desactivamos el usuario
        user.setActive(false);
        userRepository.save(user);

        log.info("Usuario borrado (desactivado) correctamente con id: {}", id);
    }

    /* Borrar Usuario (Permanente) */
    @Transactional
    public void deleteUserPermanently(Long id) {
        log.info("Usuario borrado permanentemente con id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }

        userRepository.deleteById(id);
        log.info("Usuario borrado permanentemente con id: {}", id);
    }

    /* Reactivar usuario */
    @Transactional
    public UserResponse reactivateUser(Long id) {
        log.info("Reactivando usuario con id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        user.setActive(true);
        User reactivatedUser = userRepository.save(user);

        log.info("Usuario reactivado correctamente con id: {}", reactivatedUser.getId());
        return new UserResponse(reactivatedUser);
    }
}