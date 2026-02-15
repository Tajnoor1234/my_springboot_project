package com.project.idea.controller;

import com.project.idea.model.Course1;
import com.project.idea.repository.CourseRepository;
import com.project.idea.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("newCourse", new Course1());
        return "app";
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "redirect:/teacher";
    }

    @PostMapping("/add-course")
    public String addCourse(@ModelAttribute("newCourse") Course1 course, RedirectAttributes redirectAttributes) {
        // Check if course with same ID already exists
        Course1 existingCourse = courseRepo.findByCourseId(course.getCourseId());
        if (existingCourse != null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Course with ID " + course.getCourseId() + " already exists!");
            return "redirect:/teacher";
        }

        courseRepo.save(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course added successfully!");
        return "redirect:/teacher";
    }
}