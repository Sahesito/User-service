package sahe.com.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("""
                USER SERVICE - RUNNING
                Port: 8082
                GET /users - Get all users
                GET /users/{id} - Get user by id
                POST /users - Create user
                PUT /users/{id} - Update user
                DELETE /users/{id} - Delete user
            """);
    }

}
