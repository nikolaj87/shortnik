package telrun.shortnik.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
/**
 * UserRequest class represents a request containing user information, including the user's name, password, and email.
 */
public class UserRequest {
    @Size(min = 3, max = 30, message = "username should be between 3 and 30 characters")
    private String name;

    /**
     * The user's password, which should meet the following criteria:
     * - At least 8 characters long
     * - Must include at least one uppercase letter
     * - Must include at least one lowercase letter
     * - Must include at least one number
     * - Must include at least one special character among @#$%^&+=,.?/
     * - Should not contain any whitespace characters.
     *  Pattern(
     *     regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=,.?/])(?!.*\\s).{8,}$",
     *     message = "password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character"
     * ) Specifies the pattern and error message for the field validation.
     */
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=,.?/])(?!.*\\s).{8,}$",
            message = "password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;
    /**
     * The user's email address, which should be a valid email format.
     * - The email address should consist of at least 3 characters before the '@' symbol and at least 3 characters after the '@' symbol.
     * - The email address should contain only alphanumeric characters, along with the allowed special characters +, _, ., and -.
     * Pattern(
     *     regexp = "^[A-Za-z0-9+_.-]{3,}@.{3,}$",
     *     message = "please enter a valid email"
     * ) Specifies the pattern and error message for the field validation.
     */
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
