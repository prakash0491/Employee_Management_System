
package com.employee.management.controller;

import com.employee.management.model.Document;
import com.employee.management.model.Employee;
import com.employee.management.model.User;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.service.DocumentService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    private static final String UPLOAD_DIR = "uploads/";

    private final DocumentService documentService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DocumentController(DocumentService documentService, EmployeeRepository employeeRepository) {
        this.documentService = documentService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/hr/documents")
    public String showDocumentUploadForm(Model model, HttpSession session) {
        logger.info("Accessing /hr/documents");
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            logger.warn("Unauthorized access to /hr/documents, redirecting to login");
            return "redirect:/login";
        }
        model.addAttribute("document", new Document());
        model.addAttribute("employees", employeeRepository.findAll());
        return "documents";
    }

    @PostMapping("/hr/documents/upload")
    public String uploadDocument(@RequestParam("employeeId") Long employeeId,
                                 @RequestParam("file") MultipartFile file,
                                 HttpSession session) throws IOException {
        logger.info("Uploading document for employee ID: {}", employeeId);
        User user = (User) session.getAttribute("user");
        if (user == null || !"HR".equals(user.getRole())) {
            logger.warn("Unauthorized access to /hr/documents/upload, redirecting to login");
            return "redirect:/login";
        }

        if (file.isEmpty()) {
            logger.warn("No file uploaded for employee ID: {}", employeeId);
            return "redirect:/hr/documents?error";
        }

        // Find the employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        // Save the file to the server
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, employeeId + "_" + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        // Save the document record in the database
        Document document = new Document(employee, fileName, filePath.toString());
        documentService.saveDocument(document);

        logger.info("Document uploaded successfully for employee ID: {}", employeeId);
        return "redirect:/hr/documents?success";
    }
}