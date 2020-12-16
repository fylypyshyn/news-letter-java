package com.newsletter.axon.service.dto;

import com.newsletter.axon.domain.Image;

import java.time.Instant;

public class UserFormDTO extends UserBaseDTO {

    private Long id;

    private Image image;

    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(final Image image) {
        this.image = image;
    }

    @Override
    public Instant getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(final Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "UserFormDTO{" +
                "id=" + id +
                "createdDate=" + createdDate +
                '}';
    }
}
