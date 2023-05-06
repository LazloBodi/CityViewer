package com.lazlob.cityviewer.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {
    @Id
    private String username;
    private String password;
    private String roles;

    public Set<String> getRolesSet() {
        return Set.of(roles.split(","));
    }
}
