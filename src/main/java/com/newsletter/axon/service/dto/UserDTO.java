package com.newsletter.axon.service.dto;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO extends UserBaseDTO {

    private Long id;

    @Size(min = 1, max = 50)
    private String login;

    @Size(min = 60, max = 60)
    private String password;

    private Set<AuthorityDTO> authorities;

    private boolean activated;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<AuthorityDTO> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<AuthorityDTO> authorities) {
        this.authorities = authorities;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", authorities=" + authorities +
                ", activated=" + activated +
                ", password=" + password +
                ", login=" + login +
                '}';
    }
}
