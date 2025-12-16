package com.reverso.model.enums;

public enum Role {
    USER("User", "Regular user with access to resources and events"),
    ADMIN("Admin", "Manages platform content"),
    EDITOR("Editor", "Creates and updates platform content");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
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