package de.lindemann.redcare.service.repo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class RepoScoreService {

    public Integer calcScore(@NotNull Instant createdAt,
                             @NotNull Instant updatedAt,
                             @NotNull @Min(0) Integer forksCount,
                             @NotNull @Min(0) Integer stargazersCount) {

        int hotPoints = Math.max(0, 30 - (int) Duration.between(Instant.now(), updatedAt).toDays());

        int workingMonths = (int) Period.between(
                createdAt.atZone(ZoneOffset.UTC).toLocalDate(),
                updatedAt.atZone(ZoneOffset.UTC).toLocalDate()).toTotalMonths();
        int sleepingMonths = (int) Period.between(
                updatedAt.atZone(ZoneOffset.UTC).toLocalDate(),
                Instant.now().atZone(ZoneOffset.UTC).toLocalDate()).toTotalMonths();

        int steadyPoints = Math.max(0, workingMonths - sleepingMonths);
        int forkPoints = (int) (3 * Math.log1p(forksCount)); // logarithmic scale
        int starsPoints = (int) (5 * Math.log1p(stargazersCount));

        return hotPoints + steadyPoints + forkPoints + starsPoints;
    }
}
