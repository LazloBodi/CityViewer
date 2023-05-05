package com.lazlob.cityviewer.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class City {
    @Id
    private Long id;
    private String name;
    private String photo;
}
