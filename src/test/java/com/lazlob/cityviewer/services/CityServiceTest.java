package com.lazlob.cityviewer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazlob.cityviewer.exceptions.NotFoundException;
import com.lazlob.cityviewer.models.dtos.CitiesPaginatedResponse;
import com.lazlob.cityviewer.models.dtos.CityUpdateRequest;
import com.lazlob.cityviewer.models.entities.City;
import com.lazlob.cityviewer.repositories.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {

    @Mock
    CityRepository cityRepository;
    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    CityService cityService;

    @Captor
    ArgumentCaptor<City> cityCaptor;

    @Captor
    ArgumentCaptor<Pageable> pageCaptor;

    @Test
    void getCityByIdShouldThrowNotFoundExceptionWhenCityNotExists() {
        Throwable exception = assertThrows(
                NotFoundException.class,
                () -> cityService.getCityById(0L)
        );
        assertEquals("City not found with id: 0", exception.getMessage());
    }

    @Test
    void getCityByIdShouldReturnCityWhenItIsExist() {
        City cityInDb = new City(1L, "Budapest", "budapest.jpg");
        when(cityRepository.findById(cityInDb.getId()))
                .thenReturn(Optional.of(cityInDb));

        City city = cityService.getCityById(cityInDb.getId());

        assertEquals(cityInDb.getName(), city.getName());
    }

    @Test
    void getAllCitiesPaginatedShouldReturnEmptyPaginatedResponseWhenDbIsEmpty() {
        when(cityRepository.count()).thenReturn(0L);
        when(cityRepository.findAll(pageCaptor.capture())).thenReturn(Page.empty());

        CitiesPaginatedResponse citiesPaginated = cityService.getAllCitiesPaginated(0, 100, "");

        assertEquals(0, citiesPaginated.getCities().size());
        assertEquals(0, citiesPaginated.getPage());
        assertEquals(100, citiesPaginated.getSize());
        assertEquals(0, citiesPaginated.getTotalCount());

        Pageable pageable = pageCaptor.getValue();
        assertEquals(0, pageable.getPageNumber());
        assertEquals(100, pageable.getPageSize());
        assertEquals(Sort.by("id"), pageable.getSort());
    }

    @Test
    void getAllCitiesPaginatedShouldReturnWithPaginatedResponseWhenDbIsNotEmpty() {
        List<City> cities = List.of(
                new City(1L, "Budapest", "budapest.jpg"),
                new City(2L, "Berlin", "berlin.jpg"),
                new City(3L, "London", "london.jpg"),
                new City(4L, "Paris", "paris.jpg"),
                new City(5L, "Rome", "rome.jpg"),
                new City(6L, "Madrid", "madrid.jpg")
        );
        when(cityRepository.count()).thenReturn((long) cities.size());
        Page<City> page1 = new PageImpl<>(cities.subList(0, 5));
        Page<City> page2 = new PageImpl<>(cities.subList(5, 6));
        when(cityRepository.findAll(pageCaptor.capture()))
                .thenReturn(page1)
                .thenReturn(page2);

        CitiesPaginatedResponse pageResponse1 = cityService.getAllCitiesPaginated(0, 5, "");

        assertEquals(5, pageResponse1.getCities().size());
        assertEquals(5L, pageResponse1.getCities().get(4).getId());
        assertEquals(0, pageResponse1.getPage());
        assertEquals(5, pageResponse1.getSize());
        assertEquals(cities.size(), pageResponse1.getTotalCount());

        CitiesPaginatedResponse pageResponse2 = cityService.getAllCitiesPaginated(1, 5, "");

        assertEquals(1, pageResponse2.getCities().size());
        assertEquals(6L, pageResponse2.getCities().get(0).getId());
        assertEquals(1, pageResponse2.getPage());
        assertEquals(5, pageResponse2.getSize());
        assertEquals(cities.size(), pageResponse2.getTotalCount());

        assertEquals(0,  pageCaptor.getAllValues().get(0).getPageNumber());
        assertEquals(5, pageCaptor.getAllValues().get(0).getPageSize());
        assertEquals(Sort.by("id"),  pageCaptor.getAllValues().get(0).getSort());

        assertEquals(1,  pageCaptor.getAllValues().get(1).getPageNumber());
        assertEquals(5, pageCaptor.getAllValues().get(1).getPageSize());
        assertEquals(Sort.by("id"),  pageCaptor.getAllValues().get(1).getSort());
    }

    @Test
    void getAllCitiesPaginatedShouldReturnWithPaginatedResponseWhenNameSearchIsNotEmpty() {
        String nameSearch = "bU";
        List<City> cities = List.of(
                new City(3L, "London", "london.jpg"),
                new City(4L, "Paris", "paris.jpg"),
                new City(5L, "Rome", "rome.jpg"),
                new City(1L, "Budapest", "budapest.jpg"),
                new City(2L, "Bucharest", "bucharest.jpg"),
                new City(6L, "Madrid", "madrid.jpg"),
                new City(7L, "Buenos Aires", "buenos_aires.jpg")
        );
        List<City> expectedCities = List.of(
                new City(1L, "Budapest", "budapest.jpg"),
                new City(2L, "Bucharest", "bucharest.jpg"),
                new City(7L, "Buenos Aires", "buenos_aires.jpg")
        );
        when(cityRepository.findAllByNameContainingIgnoreCase(eq(nameSearch), pageCaptor.capture()))
                .thenReturn(expectedCities);

        CitiesPaginatedResponse searchPage = cityService.getAllCitiesPaginated(0, 5, nameSearch);

        assertEquals(expectedCities.size(), searchPage.getCities().size());
        assertEquals(expectedCities, searchPage.getCities());
        assertEquals(0, searchPage.getPage());
        assertEquals(5, searchPage.getSize());
        assertEquals(expectedCities.size(), searchPage.getTotalCount());
    }

    @Test
    void updateCityShouldThrowNotFoundExceptionWhenCityNotExists() {
        Throwable exception = assertThrows(
                NotFoundException.class,
                () -> cityService.updateCity(0L, new CityUpdateRequest())
        );
        assertEquals("City not found with id: 0", exception.getMessage());
    }

    @Test
    void updateCityShouldUpDateAndReturnCityWhenExists() {
        long cityId = 1L;
        when(cityRepository.existsById(cityId)).thenReturn(true);
        City savedCity = new City(cityId, "saved", "saved.jpg");
        when(cityRepository.save(cityCaptor.capture())).thenReturn(savedCity);
        CityUpdateRequest cityUpdateRequest = new CityUpdateRequest();
        cityUpdateRequest.setName("Berlin");
        cityUpdateRequest.setPhoto("berlin.jpg");

        City returned = cityService.updateCity(cityId, cityUpdateRequest);

        assertEquals(cityId, returned.getId());
        assertEquals("saved", returned.getName());
        assertEquals("saved.jpg", returned.getPhoto());
        City capturedCity = cityCaptor.getValue();
        assertEquals(cityId, capturedCity.getId());
        assertEquals("Berlin", capturedCity.getName());
        assertEquals("berlin.jpg", capturedCity.getPhoto());
    }
}