package sahe.com.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sahe.com.userservice.dto.UserRequest;
import sahe.com.userservice.dto.UserResponse;
import sahe.com.userservice.model.User;
import sahe.com.userservice.repository.UserRepository;
import sahe.com.userservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // Obtener Usuarios
    // GET http://localhost:8082/users
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("GET /users - Get all users");
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Obtener Usuario X Id
    // GET http://localhost:8082/users/1
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("GET /users/{} - Get user by id", id);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Obtener Usuario x Email
    // GET http://localhost:8082/users/email/admin@smartcommerce.com
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("GET /users/email/{} - Get user by email", email);
        UserResponse user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    // Obtener Usuario por rol
    // GET http://localhost:8082/users/role/ADMIN
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable User.Role role) {
        log.info("GET /users/role/{} - Get users by role", role);
        List<UserResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    // Obtener Usuario por estado
    // GET http://localhost:8082/users/active/true
    @GetMapping("/active/{active}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsersByActiveStatus(@PathVariable Boolean active) {
        log.info("GET /users/active/{} - Get users by active status", active);
        List<UserResponse> users = userService.getUsersByActiveStatus(active);
        return ResponseEntity.ok(users);
    }

    // Crear Usuario
    // POST http://localhost:8082/users
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        log.info("POST /users - Create user with email: {}", request.getEmail());
        UserResponse createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Actualizar Usuario
    // PUT http://localhost:8082/users/1
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        log.info("PUT /users/{} - Update user", id);
        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    // Eliminar Usuario (Soft Delete)
    // DELETE http://localhost:8082/users/1
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /users/{} - Delete user (soft delete)", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Eliminar Usuario PERMANENTEMENTE
    // DELETE http://localhost:8082/users/1/permanent
    @DeleteMapping("/{id}/permanent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUserPermanently(@PathVariable Long id) {
        log.info("DELETE /users/{}/permanent - Delete user permanently", id);
        userService.deleteUserPermanently(id);
        return ResponseEntity.noContent().build();
    }

    // Reactivar Usuario
    // PATCH http://localhost:8082/users/1/reactivate
    @PatchMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> reactivateUser(@PathVariable Long id) {
        log.info("PATCH /users/{}/reactivate - Reactivate user", id);
        UserResponse reactivatedUser = userService.reactivateUser(id);
        return ResponseEntity.ok(reactivatedUser);
    }

    // Crear usuario desde Auth Service
    // POST http://localhost:8082/users/from-auth
    @PostMapping("/from-auth")
    public ResponseEntity<UserResponse> createUserFromAuth(@Valid @RequestBody UserRequest request) {
        log.info("POST /users/from-auth - Create user from Auth Service: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Usuario ya existe en User Service: {}", request.getEmail());
            UserResponse existingUser = userService.getUserByEmail(request.getEmail());
            return ResponseEntity.ok(existingUser);
        }
        UserResponse createdUser = userService.createUser(request);
        log.info("Usuario creado correctamente en User Service: {}", createdUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}