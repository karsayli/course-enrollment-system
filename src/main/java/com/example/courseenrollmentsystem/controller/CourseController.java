package com.example.courseenrollmentsystem.controller;

import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.dataModel.Instructor;
import com.example.courseenrollmentsystem.dto.CourseDTO;
import com.example.courseenrollmentsystem.service.CourseService;
import com.example.courseenrollmentsystem.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final InstructorService instructorService;

    @GetMapping({"", "/"})
    public String listCourses(Model model) {
        List<Course> allCourses = courseService.findAll();
        model.addAttribute("courses", allCourses);
        return "courses/list";
    }


    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("course", new CourseDTO());
        model.addAttribute("instructors", instructorService.findAll());
        return "courses/create";
    }

    @PostMapping("/create")
    public String createCourse(@ModelAttribute CourseDTO dto) {
        courseService.create(dto);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        CourseDTO dto = courseService.findById(id).orElseThrow();
        model.addAttribute("course", dto);
        model.addAttribute("instructors", instructorService.findAll());
        return "courses/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute CourseDTO dto) {
        dto.setId(id);
        courseService.update(dto);
        return "redirect:/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return "redirect:/courses";
    }
}
