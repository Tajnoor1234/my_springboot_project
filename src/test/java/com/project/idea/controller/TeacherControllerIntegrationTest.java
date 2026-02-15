package com.project.idea.controller;

import com.project.idea.model.Course1;
import com.project.idea.model.User1;
import com.project.idea.repository.CourseRepository;
import com.project.idea.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test") // This will use application-test.properties
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CourseRepository courseRepo;

    private User1 testStudent;
    private Course1 testCourse;

    @BeforeEach
    void setUp() {
        // Clear repositories before each test
        userRepo.deleteAll();
        courseRepo.deleteAll();

        // Create test student
        testStudent = new User1();
        testStudent.setUsername("test_student");
        testStudent.setPassword("password123");
        testStudent.setRole("ROLE_STUDENT");
        testStudent = userRepo.save(testStudent);

        // Create test course
        testCourse = new Course1();
        testCourse.setCourseId("TEST101");
        testCourse.setCourseName("Test Course");
        testCourse.setCredit(3);
        testCourse = courseRepo.save(testCourse);
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testTeacherDashboardIntegration() throws Exception {
        mockMvc.perform(get("/teacher"))
                .andExpect(status().isOk())
                .andExpect(view().name("app"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attribute("page", "teacher"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("newCourse"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testDeleteStudentIntegration() throws Exception {
        Long studentId = testStudent.getId();

        mockMvc.perform(post("/teacher/delete/{id}", studentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher"));

        // Verify student is deleted
        assertFalse(userRepo.findById(studentId).isPresent());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testAddCourseIntegration() throws Exception {
        mockMvc.perform(post("/teacher/add-course")
                        .param("courseId", "NEW101")
                        .param("courseName", "New Integration Course")
                        .param("credit", "4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher"));

        // Verify course was added
        Course1 savedCourse = courseRepo.findByCourseId("NEW101");
        assertNotNull(savedCourse);
        assertEquals("New Integration Course", savedCourse.getCourseName());
        assertEquals(4, savedCourse.getCredit());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testAddDuplicateCourse() throws Exception {
        // Try to add same course twice
        mockMvc.perform(post("/teacher/add-course")
                        .param("courseId", "TEST101")
                        .param("courseName", "Test Course")
                        .param("credit", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher"));

        // Verify only one course exists with this ID
        long count = courseRepo.findAll().stream()
                .filter(c -> c.getCourseId().equals("TEST101"))
                .count();
        assertEquals(1, count);
    }
}