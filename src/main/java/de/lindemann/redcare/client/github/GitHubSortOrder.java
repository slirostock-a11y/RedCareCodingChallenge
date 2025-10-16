package de.lindemann.redcare.client.github;

import lombok.Getter;

@Getter
public enum GitHubSortOrder {
    ASC("asc"),
    DESC("desc");

    private final String value;

    GitHubSortOrder(String value) {
        this.value = value;
    }

}
