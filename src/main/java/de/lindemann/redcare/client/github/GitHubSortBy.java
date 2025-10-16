package de.lindemann.redcare.client.github;

public enum GitHubSortBy {
    STARS("stars"),
    FORKS("forks"),
    HELP_WANTED_ISSUES("help-wanted-issues"),
    UPDATED("updated");

    private final String value;

    GitHubSortBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
