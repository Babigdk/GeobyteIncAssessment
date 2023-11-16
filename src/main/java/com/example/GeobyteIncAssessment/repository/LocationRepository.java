package com.example.GeobyteIncAssessment.repository;

import com.example.GeobyteIncAssessment.models.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Locations, String> {

    Boolean existsLocationsById(String id);

    Locations findLocationsById(String id);

    Boolean existsByLocationName(String name);

}
