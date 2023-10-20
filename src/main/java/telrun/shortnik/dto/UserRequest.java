package telrun.shortnik.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequest {
    @Size(min = 3, max = 30, message = "username should be between 3 and 30 characters")
    private String name;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=,.?/])(?!.*\\s).{8,}$",
            message = "password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;
    @Pattern(regexp = "^[A-Za-z0-9+_.-]{3,}@.{3,}$", message = "please enter a valid email")
    private String email;

    public UserRequest(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public UserRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
