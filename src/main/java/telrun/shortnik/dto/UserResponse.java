package telrun.shortnik.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import telrun.shortnik.entity.Role;

import java.sql.Timestamp;
import java.util.Set;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Timestamp registeredAt;
    private Set<Role> roles;

    public UserResponse(Long id, String name, String email, Timestamp registeredAt, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.registeredAt = registeredAt;
        this.roles = roles;
    }

    public UserResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


}
