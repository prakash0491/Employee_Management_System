
package com.employee.management.service;

import com.employee.management.model.Employee;
import com.employee.management.model.OnboardingRequest;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.OnboardingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnboardingService {
    private final OnboardingRequestRepository onboardingRequestRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public OnboardingService(OnboardingRequestRepository onboardingRequestRepository, EmployeeRepository employeeRepository) {
        this.onboardingRequestRepository = onboardingRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<OnboardingRequest> getAllOnboardingRequests() {
        return onboardingRequestRepository.findAll();
    }

    public void saveOnboardingRequest(OnboardingRequest onboardingRequest) {
        onboardingRequest.setStatus("PENDING");
        onboardingRequestRepository.save(onboardingRequest);
    }

    public void acceptOnboardingRequest(Long id) {
        OnboardingRequest request = onboardingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Onboarding request not found with ID: " + id));
        request.setStatus("ACCEPTED");
        onboardingRequestRepository.save(request);

        // Create a new employee from the onboarding request
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setRole(request.getRole());
        employee.setSalary(request.getSalary());
        employeeRepository.save(employee);
    }

    public void rejectOnboardingRequest(Long id) {
        OnboardingRequest request = onboardingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Onboarding request not found with ID: " + id));
        request.setStatus("REJECTED");
        onboardingRequestRepository.save(request);
    }
}