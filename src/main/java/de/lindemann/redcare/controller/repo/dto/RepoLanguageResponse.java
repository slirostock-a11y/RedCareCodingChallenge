package de.lindemann.redcare.controller.repo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing a list of programming languages from GitHub Linguist")
public class RepoLanguageResponse {

    @Schema(description = "Alphabetically sorted list of programming languages", example = "[\"Java\", \"Python\", \"C#\"]")
    private List<String> languages;
}
