
package com.employee.management.repository;

import com.employee.management.model.PerformanceRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRatingRepository extends JpaRepository<PerformanceRating, Long> {
    List<PerformanceRating> findByEmployeeId(Long employeeId);
}