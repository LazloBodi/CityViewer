package com.lazlob.cityviewer.models.dtos;

import com.lazlob.cityviewer.models.entities.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitiesPaginatedResponse {
    private Integer page;
    private Integer size;
    private Long totalCount;
    private List<City> cities;
}
