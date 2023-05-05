package com.lazlob.cityviewer.models.dtos;

import com.lazlob.cityviewer.models.entities.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CitiesPaginatedResponse {
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private List<City> cities;
}
