package org.jakubklimo.wtf.repositories;

import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByCity(City city);
}
