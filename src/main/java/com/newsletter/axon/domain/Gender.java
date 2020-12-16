package com.newsletter.axon.domain;

public enum Gender {

    MALE("male"),
    FEMALE("female");

    public final String label;

    Gender(final String label) {
        this.label = label;
    }
}
