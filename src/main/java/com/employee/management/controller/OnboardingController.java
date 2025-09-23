
package com.employee.management.controller;

import com.employee.management.model.OnboardingRequest;
import com.employee.management.model.User;
import com.employee.management.service.OnboardingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OnboardingController {
    private final OnboardingService onboardingService;

    @Autowired
    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @GetMapping("/admin/requests")
    public String manageOnboardingRequests(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("onboardingRequests", onboardingService.getAllOnboardingRequests());
        return "onboarding-requests";
    }

    @PostMapping("/admin/requests/accept/{id}")
    public String acceptOnboardingRequest(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        onboardingService.acceptOnboardingRequest(id);
        return "redirect:/admin/requests?accepted";
    }

    @PostMapping("/admin/requests/reject/{id}")
    public String rejectOnboardingRequest(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        onboardingService.rejectOnboardingRequest(id);
        return "redirect:/admin/requests?rejected";
    }

    @GetMapping("/hr/onboarding")
    public String showOnboardingForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("onboardingRequest", new OnboardingRequest());
        return "onboarding-form";
    }

    @PostMapping("/hr/onboarding")
    public String submitOnboardingRequest(OnboardingRequest onboardingRequest, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            return "redirect:/login";
        }
        onboardingService.saveOnboardingRequest(onboardingRequest);
        return "redirect:/hr/onboarding?submitted";
    }
}