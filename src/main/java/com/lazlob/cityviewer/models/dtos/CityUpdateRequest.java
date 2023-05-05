package com.lazlob.cityviewer.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CityUpdateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String photo;
}
