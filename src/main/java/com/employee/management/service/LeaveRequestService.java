
package com.employee.management.service;

import com.employee.management.model.Employee;
import com.employee.management.model.LeaveRequest;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public void saveLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequestRepository.save(leaveRequest);
    }

    public void approveLeaveRequest(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with ID: " + id));
        Employee employee = leaveRequest.getEmployee();

        // Calculate the number of leave days
        long leaveDays = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;

        // Deduct from leave balance
        int currentBalance = employee.getLeaveBalance();
        if (currentBalance < leaveDays) {
            throw new RuntimeException("Insufficient leave balance for employee: " + employee.getName());
        }
        employee.setLeaveBalance((int) (currentBalance - leaveDays));
        employeeRepository.save(employee);

        // Approve the leave request
        leaveRequest.setStatus("APPROVED");
        leaveRequestRepository.save(leaveRequest);
    }

    public void rejectLeaveRequest(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with ID: " + id));
        leaveRequest.setStatus("REJECTED");
        leaveRequestRepository.save(leaveRequest);
    }
}