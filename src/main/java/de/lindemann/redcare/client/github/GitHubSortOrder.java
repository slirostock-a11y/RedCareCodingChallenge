package de.lindemann.redcare.client.github;

public enum GitHubSortOrder {
    ASC("asc"),
    DESC("desc");

    private final String value;

    GitHubSortOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
