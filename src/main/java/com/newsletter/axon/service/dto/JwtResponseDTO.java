package com.newsletter.axon.service.dto;

import java.util.List;

public class JwtResponseDTO {

    private String token;
    private Long id;
    private String userName;
    private String email;
    private final String type = "Bearer";
    private List<String> authority;

    public JwtResponseDTO(final String token, final Long id, final String userName,
                          final String email, final List<String> authority) {
        this.token = token;
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.authority = authority;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public List<String> getAuthority() {
        return authority;
    }

    public void setAuthority(final List<String> authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "JwtResponseDTO{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", username='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", roles='" + authority + '\'' +
                '}';
    }
}
