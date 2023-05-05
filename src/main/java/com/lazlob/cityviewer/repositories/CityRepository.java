package com.lazlob.cityviewer.repositories;

import com.lazlob.cityviewer.models.entities.City;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
   List<City> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

   long countByNameContainingIgnoreCase(String name);
}
