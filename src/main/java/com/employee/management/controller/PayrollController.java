
package com.employee.management.controller;

import com.employee.management.model.Employee;
import com.employee.management.model.Payroll;
import com.employee.management.model.User;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.service.PayrollFacade;
import com.employee.management.service.PayrollService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PayrollController {
    private final PayrollService payrollService;
    private final PayrollFacade payrollFacade;
    private EmployeeRepository employeeRepository;

    @Autowired
    public PayrollController(PayrollService payrollService, PayrollFacade payrollFacade) {
        this.payrollService = payrollService;
        this.payrollFacade = payrollFacade;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/hr/payroll")
    public String showPayrollForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("payroll", new Payroll());
        return "payroll";
    }

    @PostMapping("/hr/payroll")
    public String processPayroll(@RequestParam Long employeeId, @RequestParam String month, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            return "redirect:/login";
        }
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        payrollFacade.processPayrollForEmployee(employee, month);
        return "redirect:/hr/payroll?success";
    }

    @GetMapping("/admin/payroll")
    public String viewPayrollReport(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        List<Payroll> payrolls = payrollService.getAllPayrolls();
        model.addAttribute("payrolls", payrolls);
        return "payroll-report";
    }
}
