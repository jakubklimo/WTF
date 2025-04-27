package org.jakubklimo.wtf.repositories;

import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String name);
}
