package com.lazlob.cityviewer.controllers;

import com.lazlob.cityviewer.models.dtos.CitiesPaginatedResponse;
import com.lazlob.cityviewer.models.dtos.CityResponse;
import com.lazlob.cityviewer.models.dtos.CityUpdateRequest;
import com.lazlob.cityviewer.services.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/city")
@SecurityRequirement(name = "Authorization")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CityController {

    CityService cityService;

    @Operation(summary = "Get a city by id.")
    @ApiResponse(responseCode = "200", description = "City retired successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - Please login first")
    @ApiResponse(responseCode = "404", description = "City not found")
    @GetMapping("/{id}")
    public CityResponse getCityById(@PathVariable("id") Long id) {
        return cityService.getCityById(id);
    }

    @Operation(summary = "Get all cities paginated, and optionally filtered by name.")
    @ApiResponse(responseCode = "200", description = "Cities retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Page, size or nameSearch params are invalid")
    @ApiResponse(responseCode = "401", description = "Unauthorized - Please login first")
    @GetMapping()
    public CitiesPaginatedResponse getCitiesPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) @PositiveOrZero Integer page,
            @RequestParam(value = "size", defaultValue = "100", required = false) @Positive Integer size,
            @RequestParam(value = "nameSearch", defaultValue = "", required = false) @Pattern(regexp = "^([0-9a-zA-Z\\s]{0,100})$") String nameSearch) {
        return cityService.getAllCitiesPaginated(page, size, nameSearch);
    }

    @Operation(summary = "Update a city if exists.")
    @ApiResponse(responseCode = "200", description = "City updated successfully")
    @ApiResponse(responseCode = "400", description = "City update request body is invalid")
    @ApiResponse(responseCode = "401", description = "Unauthorized - Please login first")
    @ApiResponse(responseCode = "404", description = "City not found")
    @PutMapping("/{id}")
    @Secured("ROLE_ALLOW_EDIT")
    public CityResponse updateCityById(
            @PathVariable("id") Long id,
            @RequestBody @Valid CityUpdateRequest cityUpdateRequest) {
        return cityService.updateCity(id, cityUpdateRequest);
    }

}
