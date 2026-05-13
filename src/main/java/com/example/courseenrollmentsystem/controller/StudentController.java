package com.example.courseenrollmentsystem.controller;

import com.example.courseenrollmentsystem.dto.StudentDTO;
import com.example.courseenrollmentsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping({"", "/"})
    public String listStudents(Model model) {
        System.out.println(studentService.findAll());
        model.addAttribute("students", studentService.findAll());
        return "students/list"; // students/list.html
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "students/create"; // students/create.html
    }

    @PostMapping("/create")
    public String createStudent(@ModelAttribute StudentDTO studentDTO) {
        studentService.create(studentDTO);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        StudentDTO student = studentService.findById(id).orElseThrow();
        model.addAttribute("student", student);
        return "students/edit"; // students/edit.html
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute StudentDTO studentDTO) {
        studentDTO.setId(id);
        studentService.update(studentDTO);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/students";
    }
}
