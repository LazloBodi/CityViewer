package com.lazlob.cityviewer.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityUpdateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String photo;
}
