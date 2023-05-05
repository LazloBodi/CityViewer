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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        CitiesPaginatedResponse citiesPaginated = cityService.getAllCitiesPaginated(0, 100, "");

        assertEquals(0, citiesPaginated.getCities().size());
        assertEquals(0, citiesPaginated.getPage());
        assertEquals(100, citiesPaginated.getSize());
        assertEquals(0, citiesPaginated.getTotalCount());
    }

    @Test
    void getAllCitiesPaginatedShouldReturnWithPaginatedResponseWhenDbIsNotEmpty() {
        List<City> cities = List.of(
                new City(3L, "London", "london.jpg"),
                new City(4L, "Paris", "paris.jpg"),
                new City(5L, "Rome", "rome.jpg"),
                new City(1L, "Budapest", "budapest.jpg"),
                new City(2L, "Berlin", "berlin.jpg"),
                new City(6L, "Madrid", "madrid.jpg")
        );
        when(cityRepository.count()).thenReturn((long) cities.size());
        when(cityRepository.findAll()).thenReturn(cities);

        CitiesPaginatedResponse page1 = cityService.getAllCitiesPaginated(0, 5, "");

        assertEquals(5, page1.getCities().size());
        assertEquals(5L, page1.getCities().get(4).getId());
        assertEquals(0, page1.getPage());
        assertEquals(5, page1.getSize());
        assertEquals(cities.size(), page1.getTotalCount());

        CitiesPaginatedResponse page2 = cityService.getAllCitiesPaginated(1, 5, "");

        assertEquals(1, page2.getCities().size());
        assertEquals(6L, page2.getCities().get(0).getId());
        assertEquals(1, page2.getPage());
        assertEquals(5, page2.getSize());
        assertEquals(cities.size(), page2.getTotalCount());
    }

    @Test
    void getAllCitiesPaginatedShouldReturnWithPaginatedResponseWhenNameSearchIsNotEmpty() {
        List<City> cities = List.of(
                new City(3L, "London", "london.jpg"),
                new City(4L, "Paris", "paris.jpg"),
                new City(5L, "Rome", "rome.jpg"),
                new City(1L, "Budapest", "budapest.jpg"),
                new City(2L, "Bucharest", "bucharest.jpg"),
                new City(6L, "Madrid", "madrid.jpg"),
                new City(7L, "Buenos Aires", "buenos_aires.jpg")
        );
        when(cityRepository.count()).thenReturn((long) cities.size());
        when(cityRepository.findAll()).thenReturn(cities);
        List<City> expectedCities = List.of(
                new City(1L, "Budapest", "budapest.jpg"),
                new City(2L, "Bucharest", "bucharest.jpg"),
                new City(7L, "Buenos Aires", "buenos_aires.jpg")
        );

        CitiesPaginatedResponse searchPage = cityService.getAllCitiesPaginated(0, 5, "bU");

        assertEquals(expectedCities.size(), searchPage.getCities().size());
        assertEquals(expectedCities, searchPage.getCities());
        assertEquals(0, searchPage.getPage());
        assertEquals(5, searchPage.getSize());
        assertEquals(cities.size(), searchPage.getTotalCount());
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