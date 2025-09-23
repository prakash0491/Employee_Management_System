
package com.employee.management.repository;

import com.employee.management.model.OnboardingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardingRequestRepository extends JpaRepository<OnboardingRequest, Long> {
}