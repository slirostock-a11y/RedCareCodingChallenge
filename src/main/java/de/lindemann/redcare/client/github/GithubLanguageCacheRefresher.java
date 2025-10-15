package de.lindemann.redcare.client.github;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GithubLanguageCacheRefresher {

    private final CacheManager cacheManager;

    public GithubLanguageCacheRefresher(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedDelayString = "${githubraw.language.refresh-delay}")
    public void refreshCache() {
        Cache cache = cacheManager.getCache("gitHubLanguages");
        if (cache != null) {
            cache.clear();
            log.info("GitHub language cache refreshed!");
        }
    }
}