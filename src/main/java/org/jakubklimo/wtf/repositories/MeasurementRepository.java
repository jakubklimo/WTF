package org.jakubklimo.wtf.repositories;

import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByCity(City city);

    @Query("SELECT m FROM Measurement m WHERE m.city = :city ORDER BY m.datetime DESC LIMIT 1")
    Measurement findLatestByCity(@Param("city") City city);

    @Query("SELECT m FROM Measurement m WHERE m.city = :city AND m.datetime >= :from")
    List<Measurement> findRecentMeasurements(@Param("city") City city, @Param("from") LocalDateTime from);
}
