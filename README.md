# 🔍 GitHub Repository Scoring Service

A Spring Boot backend application for **searching and scoring GitHub repositories**.  
It uses the official GitHub REST API to collect repository data and applies a custom scoring algorithm based on stars, forks, update activity, and repository lifetime.

---

## 🚀 Features

- Search public GitHub repositories using multiple filters:
    - Programming language (`language`)
    - Creation date lower bound (`createdFrom`)
    - Sorting (`stars`, `forks`, `help-wanted-issues`, `updated`)
- Compute a **custom repository score** combining:
    - Recency of updates (hotness)
    - Continuous activity (steady contributions)
    - Fork count (logarithmic scale)
    - Star count (logarithmic scale)
- Filter by **minimum score** (`minimumScore`)
- Limit the number of returned repositories (`limit`)
- Optional **GitHub Personal Access Token** (`X-GitHub-Token` header) to bypass default rate limits
- Automatic pagination up to GitHub’s 1000-repository API limit
- Swagger UI documentation for all endpoints

---

## ⚙️ Configuration

The application requires a **GitHub token** for API access.  
You can provide it as an environment variable:

```
GITHUB_TOKEN=ghp_yourGitHubTokenHere
```

The token is used as a fallback when no user token is provided via the X-GitHub-Token request header.

---

### 🧩 Profiles

The application supports two Spring profiles:

| Profile   | Description                                                  |
| --------- | ------------------------------------------------------------ |
| `default` | Standard runtime profile (production-like behavior)          |
| `dev`     | Enables developer settings, detailed logging, and Swagger UI |

Activate the dev profile using:

```
--spring.profiles.active=dev
```

in your IDE run configuration.

---

## 🧠 API Overview

Once running, the API provides endpoints for:

### 🔹 Repository Search

```
GET /api/repository/search
```

Search GitHub repositories and return a list sorted by the computed score.

#### Query & Header parameters:
| Parameter             | Type              | Description                                              |
|-----------------------|-------------------|----------------------------------------------------------|
| `language`            | String            | Filter repositories by programming language              |
| `createdFrom`         | Date (yyyy-MM-dd) | Include repositories created at this date or later       |
| `minimumScore`        | Integer           | Only include repositories with a score ≥ value           |
| `limit`               | Integer           | Limit the number of results                              |
| `repoSearchSortBy`    | Enum              | Sort field (STARS, FORKS, UPDATED, HELP_WANTED_ISSUES)   |
| `repoSearchSortOrder` | Enum              | Sort order (ASC or DESC)                                 |
| `X-GitHub-Token`      | Header            | Optional personal access token for the query             |

#### Response:
Returns a list of repositories with metadata and computed score.

---

### 🔹 Languages

```
GET /api/repository/languages
```

Retrieves the list of programming languages supported by GitHub Linguist. This allows clients to see which languages can be used when filtering repositories.

#### Response:
Returns a JSON object containing a list of language names.

```json
{
  "languages": ["Java", "Python", "C#"]
}
```

#### Notes:

* The list of languages is cached for 3 hours to reduce GitHub API calls.
* The list is fetched from the official GitHub Linguist repository.

---

## 📊 Scoring Algorithm

The score is calculated using the following logic:

```
score = hotPoints + steadyPoints + forkPoints + starsPoints
```

* Hot Points: recent activity (updates within 30 days)
* Steady Points: active vs. inactive months
* Fork Points: logarithmic weighting of forks
* Star Points: logarithmic weighting of stars

This highlights repositories that are both popular and actively maintained.

---

## 🧪 Development & Swagger

Swagger UI is automatically enabled and accessible at:

```
http://localhost:8080/swagger-ui/index.html
```

You can explore, test, and document the API interactively.

---

## 🧰 Technologies

* Java 17
* Spring Boot 3+
* Spring WebFlux (WebClient)
* Spring Cache (SimpleCache)
* Swagger / OpenAPI (Springdoc)
* Lombok
* SLF4J / Logback

---

## 🔐 Security Notes

* Always use HTTPS when providing a personal access token.
* Tokens are not logged or stored by the server — they are used only for the duration of the request.
* Do not check your GitHub token into version control.
