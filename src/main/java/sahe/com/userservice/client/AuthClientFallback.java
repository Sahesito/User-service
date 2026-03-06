package sahe.com.userservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthClientFallback implements AuthClient {

    @Override
    public void deactivateByEmail(String email) {
        log.warn("Fallback: The user could not be deactivated in auth-service: {}", email);
    }

    @Override
    public void activateByEmail(String email) {
        log.warn("Fallback: The user could not be activated in auth-service: {}", email);
    }
}