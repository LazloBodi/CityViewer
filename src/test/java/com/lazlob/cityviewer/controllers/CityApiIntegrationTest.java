package com.lazlob.cityviewer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazlob.cityviewer.models.dtos.CitiesPaginatedResponse;
import com.lazlob.cityviewer.models.dtos.CityUpdateRequest;
import com.lazlob.cityviewer.models.entities.Account;
import com.lazlob.cityviewer.models.entities.City;
import com.lazlob.cityviewer.repositories.CityRepository;
import com.lazlob.cityviewer.services.auth.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CityApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    private String adminAuthHeader;

    @BeforeEach
    public void init() {
        cityRepository.deleteAll();
        adminAuthHeader = "Bearer " + jwtTokenService.generateToken(new Account("admin", "", ""));
    }

    @Test
    void getCityByIdShouldReturnOkAndCityWhenCityExists() throws Exception {
        City testCity = new City(1L, "Budapest", "budapest.jpg");
        cityRepository.save(testCity);

        mockMvc.perform(get("/api/v1/city/" + testCity.getId())
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(testCity.getId().intValue())))
                .andExpect(jsonPath("$.name", is(testCity.getName())))
                .andExpect(jsonPath("$.photo", is(testCity.getPhoto())));
    }

    @Test
    void getCityByIdShouldReturnNotFoundWhenCityNotExists() throws Exception {
        mockMvc.perform(get("/api/v1/city/0")
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCityByIdShouldReturnBadRequestWhenCitIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/city/bp")
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCitiesPaginatedShouldReturnFirstPageWithSize100WhenParamsNotDefined() throws Exception {
        City testCity = new City(1L, "Budapest", "budapest.jpg");
        cityRepository.save(testCity);

        mockMvc.perform(get("/api/v1/city")
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(100)))
                .andExpect(jsonPath("$.totalCount", is(1)))
                .andExpect(jsonPath("$.cities[0].id", is(testCity.getId().intValue())))
                .andExpect(jsonPath("$.cities[0].name", is(testCity.getName())))
                .andExpect(jsonPath("$.cities[0].photo", is(testCity.getPhoto())));
    }

    @Test
    void getCitiesPaginatedShouldReturnFirstPageWithSize5WhenDefined() throws Exception {
        cityRepository.saveAll(List.of(
                new City(3L, "London", "london.jpg"),
                new City(4L, "Paris", "paris.jpg"),
                new City(5L, "Rome", "rome.jpg"),
                new City(1L, "Budapest", "budapest.jpg"),
                new City(2L, "Berlin", "berlin.jpg"),
                new City(6L, "Madrid", "madrid.jpg")
        ));

        MvcResult result = mockMvc.perform(get("/api/v1/city?page=0&size=5")
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.totalCount", is(6)))
                .andReturn();

        CitiesPaginatedResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), CitiesPaginatedResponse.class);
        assertEquals(5, response.getCities().size());
        assertEquals(1L, response.getCities().get(0).getId());
        assertEquals(5L, response.getCities().get(4).getId());
    }

    @Test
    void getCitiesPaginatedShouldReturnPageWhenNameSearchIsDefined() throws Exception {
        cityRepository.saveAll(List.of(
                new City(1L, "Budapest", "budapest.jpg"),
                new City(2L, "Berlin", "berlin.jpg"),
                new City(3L, "London", "london.jpg"),
                new City(4L, "Paris", "paris.jpg"),
                new City(5L, "Rome", "rome.jpg"),
                new City(6L, "Madrid", "madrid.jpg")
        ));

        MvcResult result = mockMvc.perform(get("/api/v1/city?page=0&size=5&nameSearch=Bud")
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.totalCount", is(1)))
                .andReturn();

        CitiesPaginatedResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), CitiesPaginatedResponse.class);
        assertEquals(1, response.getCities().size());
        assertEquals("Budapest", response.getCities().get(0).getName());
    }

    @Test
    void updateCityByIdShouldUpdateCityAndReturnOkWhenCityExists() throws Exception {
        City cityInDb = new City(1L, "Budapest", "budapest.jpg");
        cityRepository.save(cityInDb);
        CityUpdateRequest cityUpdateRequest = new CityUpdateRequest("Berlin", "berlin.jpg");

        mockMvc.perform(put("/api/v1/city/1")
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader)
                        .content(objectMapper.writeValueAsString(cityUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(cityInDb.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Berlin")))
                .andExpect(jsonPath("$.photo", is("berlin.jpg")));

        City city = cityRepository.findById(1L).get();
        assertEquals("Berlin", city.getName());
        assertEquals("berlin.jpg", city.getPhoto());
    }

    @Test
    void updateCityByIdShouldReturnNotFoundWhenCityNotExists() throws Exception {
        CityUpdateRequest cityUpdateRequest = new CityUpdateRequest("Budapest", "budapest.jpg");

        mockMvc.perform(put("/api/v1/city/0")
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader)
                        .content(objectMapper.writeValueAsString(cityUpdateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCityByIdShouldReturnForbiddenWhenUserNotHaveAuthority() throws Exception {
        String userAuthHeader = "Bearer " + jwtTokenService.generateToken(new Account("user", "", ""));
        CityUpdateRequest cityUpdateRequest = new CityUpdateRequest("Budapest", "budapest.jpg");

        mockMvc.perform(put("/api/v1/city/0")
                        .contentType("application/json")
                        .header("Authorization", userAuthHeader)
                        .content(objectMapper.writeValueAsString(cityUpdateRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateCityByIdShouldReturnBadRequestWhenRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(put("/api/v1/city/0")
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCityByIdShouldReturnBadRequestWhenNameIsMissing() throws Exception {
        mockMvc.perform(put("/api/v1/city/0")
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader)
                        .content("{photo: \"budapest.jpg\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCityByIdShouldReturnBadRequestWhenPhotoIsMissing() throws Exception {
        mockMvc.perform(put("/api/v1/city/0")
                        .contentType("application/json")
                        .header("Authorization", adminAuthHeader)
                        .content("{name: \"Budapest\"}"))
                .andExpect(status().isBadRequest());
    }
}