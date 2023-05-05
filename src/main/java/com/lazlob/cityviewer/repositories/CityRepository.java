package com.lazlob.cityviewer.repositories;

import com.lazlob.cityviewer.models.entities.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {
}
