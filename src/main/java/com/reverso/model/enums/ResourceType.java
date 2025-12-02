package com.reverso.model.enums;

public enum ResourceType {
    GUIDE("Guide", "Practical guidance documents"),
    REPORT("Report", "Research or analytical reports"),
    ARTICLE("Article", "Blog-type written content"),
    VIDEO("Video", "Multimedia video resources"),
    OTHER("Other", "Any other kind of resource");

    private final String displayName;
    private final String description;

    ResourceType(String displayName, String description) {
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