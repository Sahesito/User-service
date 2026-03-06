package sahe.com.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "auth-service", fallback = AuthClientFallback.class)
public interface AuthClient {

    @PutMapping("/auth/users/email/{email}/deactivate")
    void deactivateByEmail(@PathVariable String email);

    @PutMapping("/auth/users/email/{email}/activate")
    void activateByEmail(@PathVariable String email);
}