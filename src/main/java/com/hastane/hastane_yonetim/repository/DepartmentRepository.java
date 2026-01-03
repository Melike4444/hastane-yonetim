package com.hastane.hastane_yonetim.repository;

import com.hastane.hastane_yonetim.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByAdIgnoreCase(String ad);
    boolean existsByAdIgnoreCase(String ad);
}
