package com.example.courseenrollmentsystem.controller;

import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.dto.EnrollmentDTO;
import com.example.courseenrollmentsystem.dto.CourseDTO;
import com.example.courseenrollmentsystem.dto.StudentDTO;
import com.example.courseenrollmentsystem.service.CourseService;
import com.example.courseenrollmentsystem.service.EnrollmentService;
import com.example.courseenrollmentsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("enrollment", new EnrollmentDTO());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "enrollments/create";
    }

    @PostMapping("/create")
    public String createEnrollment(@ModelAttribute EnrollmentDTO dto, Model model) {
        try {
            enrollmentService.create(dto);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return createForm(model);
        }
        return "redirect:/enrollments";
    }

    @GetMapping({"", "/"})
    public String listEnrollments(Model model) {
        model.addAttribute("enrollments", enrollmentService.findAll());
        return "enrollments/list";
    }

    @GetMapping("/edit/{id}")
    public String editEnrollmentForm(@PathVariable Long id, Model model) {
        Enrollment enrollment = enrollmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        System.out.println(enrollment);

        model.addAttribute("enrollment", enrollment);

        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "enrollments/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEnrollment(@PathVariable Long id, @ModelAttribute EnrollmentDTO dto, Model model) {
        try {
            enrollmentService.update(dto);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return editEnrollmentForm(id, model);
        }
        return "redirect:/enrollments";
    }

    @GetMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable Long id) {
        enrollmentService.delete(id);
        return "redirect:/enrollments";
    }
}
