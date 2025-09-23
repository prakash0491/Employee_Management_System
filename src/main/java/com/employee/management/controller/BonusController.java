
package com.employee.management.controller;

import com.employee.management.model.Employee;
import com.employee.management.model.User;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.service.BonusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BonusController {
    private final EmployeeRepository employeeRepository;
    private final BonusService bonusService;

    @Autowired
    public BonusController(EmployeeRepository employeeRepository, BonusService bonusService) {
        this.employeeRepository = employeeRepository;
        this.bonusService = bonusService;
    }

    @GetMapping("/hr/bonus")
    public String showBonusPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("employees", employeeRepository.findAll());
        return "bonus";
    }

    @PostMapping("/hr/bonus/calculate/{id}")
    public String calculateBonus(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            return "redirect:/login";
        }
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        double bonus = bonusService.calculateBonus(employee);
        employee.setBonus(bonus);
        employeeRepository.save(employee);
        return "redirect:/hr/bonus?calculated";
    }
}