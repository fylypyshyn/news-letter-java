package com.newsletter.axon.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class UserBase extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    @NotNull
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    @NotNull
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    @NotNull
    private String email;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    @NotNull
    @Column(name = "address", length = 50)
    private String address;

    @NotNull
    @Column(name = "gender", length = 6)
    private Gender gender;

    @NotNull
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(final String langKey) {
        this.langKey = langKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UserBase userBase = (UserBase) o;
        return Objects.equals(getFirstName(), userBase.getFirstName()) && Objects.equals(getLastName(), userBase.getLastName()) && Objects.equals(getEmail(), userBase.getEmail()) && Objects.equals(getLangKey(), userBase.getLangKey()) && Objects.equals(getAddress(), userBase.getAddress()) && getGender() == userBase.getGender() && Objects.equals(getPhoneNumber(), userBase.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getEmail(), getLangKey(), getAddress(), getGender(), getPhoneNumber());
    }

    @Override
    public String toString() {
        return "UserBase{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", langKey='" + langKey + '\'' +
                '}';
    }
}
