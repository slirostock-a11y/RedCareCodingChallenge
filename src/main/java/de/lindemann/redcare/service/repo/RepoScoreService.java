package de.lindemann.redcare.service.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class RepoScoreService {

    public Integer calcScore(Instant createdAt,
                             Instant updatedAt,
                             Integer forksCount,
                             Integer stargazersCount) {

        if (createdAt == null) throw new IllegalArgumentException("createdAt must not be null");
        if (updatedAt == null) throw new IllegalArgumentException("updatedAt must not be null");
        if (forksCount == null || forksCount < 0) throw new IllegalArgumentException("forksCount must be >= 0");
        if (stargazersCount == null || stargazersCount < 0)
            throw new IllegalArgumentException("stargazersCount must be >= 0");

        int hotPoints = Math.max(0, 30 - (int) Duration.between(updatedAt, Instant.now()).toDays());

        int workingMonths = (int) Period.between(
                createdAt.atZone(ZoneOffset.UTC).toLocalDate(),
                updatedAt.atZone(ZoneOffset.UTC).toLocalDate()).toTotalMonths();
        int sleepingMonths = (int) Period.between(
                updatedAt.atZone(ZoneOffset.UTC).toLocalDate(),
                Instant.now().atZone(ZoneOffset.UTC).toLocalDate()).toTotalMonths();

        int steadyPoints = Math.max(0, workingMonths - 2 * sleepingMonths);
        int forkPoints = (int) (3 * Math.log1p(forksCount)); // logarithmic scale
        int starsPoints = (int) (5 * Math.log1p(stargazersCount));

        return hotPoints + steadyPoints + forkPoints + starsPoints;
    }

}
