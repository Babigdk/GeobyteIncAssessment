package com.example.GeobyteIncAssessment.repository;

import com.example.GeobyteIncAssessment.models.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {

    Boolean existsStaffByEmail(String email);

    Staff findStaffById(String id);
}
