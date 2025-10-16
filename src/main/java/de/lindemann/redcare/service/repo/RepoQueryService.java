package de.lindemann.redcare.service.repo;

import de.lindemann.redcare.controller.repo.dto.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RepoQueryService {

    public String generateQuery(SearchRequest repoRequest) {
        return Stream.of(
                        "is:public",
                        generateLanguage(repoRequest.getLanguage()),
                        generateCreatedFrom(repoRequest.getCreatedFrom())
                )
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("+"));
    }

    private String generateCreatedFrom(LocalDate createdAfter) {
        return createdAfter != null ? "created:>=" + createdAfter : StringUtils.EMPTY;
    }

    private String generateLanguage(String language) {
        return StringUtils.isBlank(language) ? StringUtils.EMPTY : ("language:" + "\"" + language + "\"");
    }
}
