package com.employee.management.service;

import com.employee.management.model.Employee;
import com.employee.management.model.LeaveRequest;

public class ManualApproveStrategy implements LeaveApprovalStrategy {
    @Override
    public void approve(LeaveRequest request, Employee employee, long days) {
        if (employee.getLeaveBalance() >= days) {
            employee.setLeaveBalance(employee.getLeaveBalance() - (int) days);
            request.setStatus("APPROVED");
        } else {
            request.setStatus("REJECTED");
        }
    }
}