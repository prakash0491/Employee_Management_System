
package com.employee.management.controller;

import com.employee.management.model.Employee;
import com.employee.management.model.LeaveRequest;
import com.employee.management.model.User;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.service.LeaveRequestService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LeaveController {
    private static final Logger logger = LoggerFactory.getLogger(LeaveController.class);
    
    private final LeaveRequestService leaveRequestService;
    private final EmployeeRepository employeeRepository;
    private final Validator validator; // Add Validator for manual validation

    @Autowired
    public LeaveController(LeaveRequestService leaveRequestService, EmployeeRepository employeeRepository, Validator validator) {
        this.leaveRequestService = leaveRequestService;
        this.employeeRepository = employeeRepository;
        this.validator = validator;
    }

    @GetMapping("/hr/leaves")
    public String manageLeaves(Model model, HttpSession session) {
        logger.info("Accessing /hr/leaves");
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            logger.warn("Unauthorized access to /hr/leaves, redirecting to login");
            return "redirect:/login";
        }
        model.addAttribute("leaveRequests", leaveRequestService.getAllLeaveRequests());
        return "leaves";
    }

    @PostMapping("/hr/leaves/approve/{id}")
    public String approveLeave(@PathVariable Long id, HttpSession session) {
        logger.info("Approving leave request with ID: {}", id);
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            logger.warn("Unauthorized access to approve leave, redirecting to login");
            return "redirect:/login";
        }
        leaveRequestService.approveLeaveRequest(id);
        return "redirect:/hr/leaves?approved";
    }

    @PostMapping("/hr/leaves/reject/{id}")
    public String rejectLeave(@PathVariable Long id, HttpSession session) {
        logger.info("Rejecting leave request with ID: {}", id);
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            logger.warn("Unauthorized access to reject leave, redirecting to login");
            return "redirect:/login";
        }
        leaveRequestService.rejectLeaveRequest(id);
        return "redirect:/hr/leaves?rejected";
    }

    @GetMapping("/admin/leave/submit")
    public String showLeaveRequestForm(Model model, HttpSession session) {
        logger.info("Accessing /admin/leave/submit");
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            logger.warn("Unauthorized access to /admin/leave/submit, redirecting to login");
            return "redirect:/login";
        }
        try {
            model.addAttribute("leaveRequest", new LeaveRequest());
            model.addAttribute("employees", employeeRepository.findAll());
            if (employeeRepository.findAll().isEmpty()) {
                logger.warn("No employees found in the database");
                model.addAttribute("noEmployees", true);
            }
            logger.info("Successfully prepared leave request form");
        } catch (Exception e) {
            logger.error("Error while preparing leave request form", e);
            throw e;
        }
        return "leave-request-form";
    }

    @PostMapping("/admin/leave/submit")
    public String submitLeaveRequest(LeaveRequest leaveRequest, 
                                     @RequestParam("employeeId") Long employeeId, HttpSession session, Model model) {
        logger.info("Submitting leave request for employee ID: {}", employeeId);
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            logger.warn("Unauthorized access to submit leave, redirecting to login");
            return "redirect:/login";
        }

        // Set the employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        leaveRequest.setEmployee(employee);

        // Manually validate the LeaveRequest
        BindingResult result = new BeanPropertyBindingResult(leaveRequest, "leaveRequest");
        validator.validate(leaveRequest, result);

        if (result.hasErrors()) {
            logger.warn("Validation errors in leave request submission: {}", result.getAllErrors());
            model.addAttribute("employees", employeeRepository.findAll());
            return "leave-request-form";
        }

        leaveRequest.setStatus("PENDING");
        leaveRequestService.saveLeaveRequest(leaveRequest);
        logger.info("Leave request submitted successfully");
        return "redirect:/admin/leave/submit?submitted";
    }
}