
package com.employee.management.service;

import com.employee.management.model.Employee;
import com.employee.management.model.Payroll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PayrollFacade {
    private final PayrollService payrollService;

    @Autowired
    public PayrollFacade(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    public void processPayrollForEmployee(Employee employee, String month) {
        // Check if payroll already exists for the month
        Optional<Payroll> existingPayroll = payrollService.getPayrollByMonth(month);
        if (existingPayroll.isPresent()) {
            throw new RuntimeException("Payroll already processed for month: " + month);
        }
        payrollService.processPayroll(employee.getId(), month);
    }
}