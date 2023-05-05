package com.lazlob.cityviewer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazlob.cityviewer.exceptions.NotFoundException;
import com.lazlob.cityviewer.models.dtos.CitiesPaginatedResponse;
import com.lazlob.cityviewer.models.dtos.CityResponse;
import com.lazlob.cityviewer.models.dtos.CityUpdateRequest;
import com.lazlob.cityviewer.models.entities.City;
import com.lazlob.cityviewer.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CityService {

    CityRepository cityRepository;
    ObjectMapper objectMapper;

    public CityResponse getCityById(Long id) {
        return cityRepository.findById(id)
                .map(c -> objectMapper.convertValue(c, CityResponse.class))
                .orElseThrow(() -> new NotFoundException("City not found with id: " + id));
    }

    public CitiesPaginatedResponse getAllCitiesPaginated(Integer page, Integer size, String nameSearch) {
        List<City> cities;
        long totalCount;
        if (StringUtils.isNotBlank(nameSearch.trim())) {
            cities = cityRepository.findAllByNameContainingIgnoreCase(nameSearch.trim(), PageRequest.of(page, size, Sort.by("id")));
            totalCount = cityRepository.countByNameContainingIgnoreCase(nameSearch.trim());
        } else {
            cities = StreamSupport.stream(cityRepository.findAll(PageRequest.of(page, size, Sort.by("id"))).spliterator(), false).toList();
            totalCount = cityRepository.count();
        }

        return CitiesPaginatedResponse.builder()
                .page(page).size(size).totalCount(totalCount)
                .cities(cities.stream().map(c -> objectMapper.convertValue(c, CityResponse.class)).toList())
                .build();
    }

    public CityResponse updateCity(Long id, CityUpdateRequest cityUpdateRequest) {
        City city = objectMapper.convertValue(cityUpdateRequest, City.class);
        city.setId(id);
        if (!cityRepository.existsById(city.getId())) {
            throw new NotFoundException("City not found with id: " + city.getId());
        }
        City savedCity = cityRepository.save(city);
        return objectMapper.convertValue(savedCity, CityResponse.class);
    }
}
