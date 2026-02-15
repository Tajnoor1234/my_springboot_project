package com.project.idea.controller;

import com.project.idea.model.Course1;
import com.project.idea.repository.CourseRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final CourseRepository courseRepo;

    public StudentController(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    @GetMapping
    public String studentDashboard(Model model, HttpSession session) {
        model.addAttribute("page", "student");

        // Get logged-in student's username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentName = auth.getName(); // This gets the username
        model.addAttribute("studentName", studentName);

        // Get all available courses from database
        model.addAttribute("availableCourses", courseRepo.findAll());

        // Get selected courses from session or create empty list
        List<Course1> selectedCourses = (List<Course1>) session.getAttribute("selectedCourses");
        if (selectedCourses == null) {
            selectedCourses = new ArrayList<>();
            session.setAttribute("selectedCourses", selectedCourses);
        }
        model.addAttribute("selectedCourses", selectedCourses);

        // Default to showing "my courses" section
        model.addAttribute("activeSection", "myCourses");

        return "app";
    }

    @PostMapping("/add-course")
    public String addCourseToStudent(@RequestParam Long courseId, HttpSession session) {
        Course1 course = courseRepo.findById(courseId).orElse(null);

        if (course != null) {
            List<Course1> selectedCourses = (List<Course1>) session.getAttribute("selectedCourses");
            if (selectedCourses == null) {
                selectedCourses = new ArrayList<>();
            }

            boolean alreadyExists = selectedCourses.stream()
                    .anyMatch(c -> c.getId().equals(courseId));

            if (!alreadyExists) {
                selectedCourses.add(course);
                session.setAttribute("selectedCourses", selectedCourses);
            }
        }

        return "redirect:/student";
    }

    @PostMapping("/remove-course")
    public String removeCourseFromStudent(@RequestParam Long courseId, HttpSession session) {
        List<Course1> selectedCourses = (List<Course1>) session.getAttribute("selectedCourses");
        if (selectedCourses != null) {
            selectedCourses.removeIf(course -> course.getId().equals(courseId));
            session.setAttribute("selectedCourses", selectedCourses);
        }
        return "redirect:/student";
    }

    @GetMapping("/my-courses")
    public String showMyCourses(HttpSession session, Model model) {
        model.addAttribute("page", "student");

        // Get logged-in student's username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentName = auth.getName();
        model.addAttribute("studentName", studentName);

        // Get selected courses
        List<Course1> selectedCourses = (List<Course1>) session.getAttribute("selectedCourses");
        if (selectedCourses == null) {
            selectedCourses = new ArrayList<>();
        }
        model.addAttribute("selectedCourses", selectedCourses);

        // Set active section
        model.addAttribute("activeSection", "myCourses");

        return "app";
    }

    @GetMapping("/available-courses")
    public String showAvailableCourses(HttpSession session, Model model) {
        model.addAttribute("page", "student");

        // Get logged-in student's username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentName = auth.getName();
        model.addAttribute("studentName", studentName);

        // Get all available courses
        model.addAttribute("availableCourses", courseRepo.findAll());

        // Get selected courses (to check which are already selected)
        List<Course1> selectedCourses = (List<Course1>) session.getAttribute("selectedCourses");
        if (selectedCourses == null) {
            selectedCourses = new ArrayList<>();
        }
        model.addAttribute("selectedCourses", selectedCourses);

        // Set active section
        model.addAttribute("activeSection", "availableCourses");

        return "app";
    }
}