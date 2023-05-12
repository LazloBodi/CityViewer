package com.lazlob.cityviewer.models.dtos;

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
    private String name;
    @NotBlank
    private String photo;
}
