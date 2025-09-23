
package com.employee.management.service;

import com.employee.management.model.LeaveRequest;
import com.employee.management.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {
    private final LeaveRequestRepository leaveRequestRepository;

    @Autowired
    public LeaveService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public void approveLeaveRequest(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with ID: " + id));
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
