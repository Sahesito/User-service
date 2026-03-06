package sahe.com.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sahe.com.userservice.client.AuthClient;
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
    private final AuthClient authClient;

    public List<UserResponse> getAllUsers() {
        log.info("Extracting all users");
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        log.info("Searching for user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found by ID: " + id));
        return new UserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        log.info("Searching for user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found by email: " + email));
        return new UserResponse(user);
    }

    public List<UserResponse> getUsersByRole(User.Role role) {
        log.info("Searching user by role: {}", role);
        return userRepository.findByRole(role)
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getUsersByActiveStatus(Boolean active) {
        log.info("Searching user by state: {}", active);
        return userRepository.findByActive(active)
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        log.info("Creating a user with the email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("The email already exists: " + request.getEmail());
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
        log.info("User successfully created id: {}", savedUser.getId());

        return new UserResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        log.info("Updating user by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID not found: " + id));
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Existing email: " + request.getEmail());
        }

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
        log.info("User successfully updated with id: {}", updatedUser.getId());

        return new UserResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setActive(false);
        userRepository.save(user);

        try {
            authClient.deactivateByEmail(user.getEmail());
            log.info("User also disabled in auth-service: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error disabling in auth-service: {}", e.getMessage());
        }

        log.info("User successfully deleted (deactivated) with id: {}", id);
    }

    @Transactional
    public void deleteUserPermanently(Long id) {
        log.info("User permanently deleted with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.info("User permanently deleted with id: {}", id);
    }

    @Transactional
    public UserResponse reactivateUser(Long id) {
        log.info("Reactivating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setActive(true);
        User reactivatedUser = userRepository.save(user);

        try {
            authClient.activateByEmail(user.getEmail());
            log.info("User also reactivated in auth-service: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error reactivating in auth-service: {}", e.getMessage());
        }

        log.info("User successfully reactivated with id: {}", reactivatedUser.getId());
        return new UserResponse(reactivatedUser);
    }
}