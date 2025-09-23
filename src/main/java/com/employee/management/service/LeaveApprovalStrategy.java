package com.employee.management.service;

import com.employee.management.model.Employee;
import com.employee.management.model.LeaveRequest;

public interface LeaveApprovalStrategy {
    void approve(LeaveRequest request, Employee employee, long days);
}
