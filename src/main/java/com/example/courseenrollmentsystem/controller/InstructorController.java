package com.example.courseenrollmentsystem.controller;

import com.example.courseenrollmentsystem.dto.InstructorDTO;
import com.example.courseenrollmentsystem.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @GetMapping({"", "/"})
    public String listInstructors(Model model) {
        model.addAttribute("instructors", instructorService.findAll());
        return "instructors/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("instructor", new InstructorDTO());
        return "instructors/create";
    }

    @PostMapping("/create")
    public String createInstructor(@ModelAttribute InstructorDTO dto) {
        instructorService.create(dto);
        return "redirect:/instructors";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        InstructorDTO dto = instructorService.findById(id).orElseThrow();
        model.addAttribute("instructor", dto);
        return "instructors/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateInstructor(@PathVariable Long id, @ModelAttribute InstructorDTO dto) {
        dto.setId(id);
        instructorService.update(dto);
        return "redirect:/instructors";
    }

    @GetMapping("/delete/{id}")
    public String deleteInstructor(@PathVariable Long id) {
        instructorService.delete(id);
        return "redirect:/instructors";
    }
}

