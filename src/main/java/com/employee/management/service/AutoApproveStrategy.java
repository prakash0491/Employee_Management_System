
package com.employee.management.service;

import com.employee.management.model.Employee;
import com.employee.management.model.LeaveRequest;

public class AutoApproveStrategy implements LeaveApprovalStrategy {
    @Override
    public void approve(LeaveRequest request, Employee employee, long days) {
        employee.setLeaveBalance(employee.getLeaveBalance() - (int) days);
        request.setStatus("APPROVED");
    }
}