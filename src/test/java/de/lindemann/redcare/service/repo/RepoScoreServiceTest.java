package de.lindemann.redcare.service.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RepoScoreServiceTest {

    private final RepoScoreService service = new RepoScoreService();

    @Test
    void calcScore_basicCalculation() {
        Instant createdAt = Instant.now().minus(365, ChronoUnit.DAYS);
        Instant updatedAt = Instant.now().minus(15, ChronoUnit.DAYS);

        int forks = 224;
        int stars = 112;

        Integer score = service.calcScore(createdAt, updatedAt, forks, stars);

        // Calculate expected values manually
        int hotPoints = 15;
        int workingMonths = 11;
        int sleepingMonths = 0;
        int steadyPoints = workingMonths - sleepingMonths;
        int forkPoints = (int) (3 * Math.log1p(forks)); // 16
        int starsPoints = (int) (5 * Math.log1p(stars)); // 23

        int expectedScore = hotPoints + steadyPoints + forkPoints + starsPoints;

        Assertions.assertEquals(expectedScore, score);
    }

    @Test
    void calcScore_shouldThrowIfCreatedAtIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.calcScore(null, Instant.now(), 1, 1));
    }

    @Test
    void calcScore_shouldThrowIfUpdatedAtIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.calcScore(Instant.now(), null, 1, 1));
    }

    @Test
    void calcScore_shouldThrowIfForksNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.calcScore(Instant.now(), Instant.now(), -1, 1));
    }

    @Test
    void calcScore_shouldThrowIfStarsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.calcScore(Instant.now(), Instant.now(), 1, -5));
    }

    @Test
    void calcScore_withZeroForksAndStars() {
        Instant createdAt = Instant.now().minus(100, ChronoUnit.DAYS);
        Instant updatedAt = Instant.now().minus(1, ChronoUnit.DAYS);

        int score = service.calcScore(createdAt, updatedAt, 0, 0);

        int hotPoints = 29;
        int workingMonths = 3;
        int sleepingMonths = 0;
        int steadyPoints = workingMonths - sleepingMonths;
        int forkPoints = 0;
        int starsPoints = 0;

        int expectedScore = hotPoints + steadyPoints + forkPoints + starsPoints;

        Assertions.assertEquals(expectedScore, score);
    }

    @Test
    void calcScore_oldUnmaintainedRepoShouldHaveLowScore() {
        Instant createdAt = Instant.now().minus(3000, ChronoUnit.DAYS);
        Instant updatedAt = Instant.now().minus(2000, ChronoUnit.DAYS);

        int score = service.calcScore(createdAt, updatedAt, 5, 10);
        Assertions.assertTrue(score < 20, "Old, unmaintained repos should have low scores");
    }
}
