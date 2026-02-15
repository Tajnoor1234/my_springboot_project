package com.project.idea.controller;

import com.project.idea.model.Course1;
import com.project.idea.repository.CourseRepository;
import com.project.idea.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final UserRepository userRepo;
    private final CourseRepository courseRepo;

    public TeacherController(UserRepository userRepo, CourseRepository courseRepo) {
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
    }

    @GetMapping
    public String teacherDashboard(Model model) {
        model.addAttribute("page", "teacher");
        model.addAttribute("students", userRepo.findByRole("ROLE_STUDENT"));
        model.addAttribute("courses", courseRepo.findAll());
        // Add this line - create a new Course1 object for the form
        model.addAttribute("newCourse", new Course1());
        return "app";
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "redirect:/teacher";
    }

    @PostMapping("/add-course")
    public String addCourse(@ModelAttribute("newCourse") Course1 course) {
        courseRepo.save(course);
        return "redirect:/teacher";
    }
}