package com.newsletter.axon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "axon_user_form")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserForm extends UserBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(nullable = false)

    private boolean subscribed = false;

    @JsonIgnore
    @OneToOne
    @JoinTable(
            name = "axon_user_form_image",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "image_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Image image;

    public UserForm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(final boolean subscribe) {
        this.subscribed = subscribe;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(final Image image) {
        this.image = image;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final UserForm userForm = (UserForm) o;
        return isSubscribed() == userForm.isSubscribed() && Objects.equals(getId(), userForm.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), isSubscribed());
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "id=" + id +
                ", subscribe=" + subscribed +
                '}';
    }
}
