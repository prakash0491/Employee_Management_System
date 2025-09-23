
package com.employee.management.service;

import com.employee.management.model.Employee;
import com.employee.management.model.Payroll;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayrollService {
    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public PayrollService(PayrollRepository payrollRepository, EmployeeRepository employeeRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
    }

    public void savePayroll(Payroll payroll) {
        payrollRepository.save(payroll);
    }

    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    public void processPayroll(Long employeeId, String month) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        Payroll payroll = new Payroll();
        payroll.setEmployee(employee);
        payroll.setMonth(month);
        // Include salary and bonus in the payroll amount
        double totalAmount = employee.getSalary() + employee.getBonus();
        payroll.setAmount(totalAmount);
        payroll.setStatus("PROCESSED");
        payrollRepository.save(payroll);
    }

    public Optional<Payroll> getPayrollByMonth(String month) {
        return payrollRepository.findByMonth(month);
    }
}