package com.newsletter.axon.service.dto;

import javax.validation.constraints.Size;

public class AuthorityDTO {

    @Size(max = 50)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AuthorityDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}
