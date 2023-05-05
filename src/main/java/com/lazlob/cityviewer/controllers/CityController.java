package com.lazlob.cityviewer.controllers;

import com.lazlob.cityviewer.models.dtos.CitiesPaginatedResponse;
import com.lazlob.cityviewer.models.dtos.CityUpdateRequest;
import com.lazlob.cityviewer.models.entities.City;
import com.lazlob.cityviewer.services.CityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/city")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CityController {

    CityService cityService;

    @GetMapping("/{id}")
    public City getCityById(@PathVariable("id") Long id) {
        return cityService.getCityById(id);
    }

    @GetMapping()
    public CitiesPaginatedResponse getCitiesPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) @PositiveOrZero Integer page,
            @RequestParam(value = "size", defaultValue = "100", required = false) @Positive Integer size,
            @RequestParam(value = "nameSearch", defaultValue ="", required = false) String nameSearch) {
        return cityService.getAllCitiesPaginated(page, size, nameSearch);
    }

    @PutMapping("/{id}")
    public City updateCityById(
            @PathVariable("id") Long id,
            @RequestBody @Valid CityUpdateRequest cityUpdateRequest) {
        return cityService.updateCity(id, cityUpdateRequest);
    }

}
