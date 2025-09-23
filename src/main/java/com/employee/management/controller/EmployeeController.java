
package com.employee.management.controller;

import com.employee.management.model.Employee;
import com.employee.management.model.PerformanceRating;
import com.employee.management.model.User;
import com.employee.management.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/admin/employees")
    public String listEmployees(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees";
    }

    @GetMapping("/admin/employees/add")
    public String showAddForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    @PostMapping("/admin/employees/save")
    public String saveEmployee(Employee employee, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        employeeService.addEmployee(employee);
        return "redirect:/admin/employees?added";
    }

    @GetMapping("/admin/employees/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employee-form";
    }

    @PostMapping("/admin/employees/update/{id}")
    public String updateEmployee(@PathVariable Long id, Employee employee, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        employeeService.updateEmployee(id, employee);
        return "redirect:/admin/employees?updated";
    }

    @PostMapping("/admin/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        employeeService.deleteEmployee(id);
        return "redirect:/admin/employees?deleted";
    }

    @GetMapping("/admin/employees/rating/{id}")
    public String showRatingForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        model.addAttribute("rating", new PerformanceRating());
        return "rating-form";
    }

    @PostMapping("/admin/employees/rating/{id}")
    public String submitRating(@PathVariable Long id, PerformanceRating rating, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        rating.setEmployeeId(id);
        employeeService.submitPerformanceRating(rating);
        return "redirect:/admin/employees?rated";
    }
}