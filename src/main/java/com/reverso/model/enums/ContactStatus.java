package com.reverso.model.enums;

public enum ContactStatus {
    PENDING("Pending", "Message received and waiting to be processed"),
    IN_PROGRESS("In Progress", "Message is being reviewed"),
    RESOLVED("Resolved", "Inquiry or issue has been solved");

    private final String displayName;
    private final String description;

    ContactStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}