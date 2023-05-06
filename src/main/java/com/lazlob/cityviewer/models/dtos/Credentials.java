package com.lazlob.cityviewer.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Credentials {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
