package com.newsletter.axon.service.dto;

public class MessageResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public MessageResponse(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
