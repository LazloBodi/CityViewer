package com.lazlob.cityviewer.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityUpdateRequest {
    @NotBlank
    @Pattern(regexp = "^([0-9a-zA-Z\\s]{0,100})$")
    @Schema(description = "Name of city", example = "Budapest")
    private String name;
    @NotBlank
    @Schema(description = "Url for photo of the city", example = "url")
    private String photo;
}
