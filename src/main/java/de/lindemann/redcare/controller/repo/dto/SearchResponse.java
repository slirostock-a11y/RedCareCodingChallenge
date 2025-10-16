package de.lindemann.redcare.controller.repo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(description = "Response containing found repositories")
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    private Integer size;
    private List<SearchRepositoryResponse> repositories;

}
