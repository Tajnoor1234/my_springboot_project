package com.project.idea.repository;

import com.project.idea.model.Course1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void testSaveAndFindCourse() {
        Course1 course = new Course1();
        course.setCourseId("CS101");
        course.setCourseName("Java Programming");
        course.setCredit(3);

        Course1 savedCourse = courseRepository.save(course);

        assertNotNull(savedCourse.getId());

        Optional<Course1> found = courseRepository.findById(savedCourse.getId());
        assertTrue(found.isPresent());
        assertEquals("CS101", found.get().getCourseId());
    }

    @Test
    void testFindByCourseId() {
        Course1 course = new Course1();
        course.setCourseId("MATH201");
        course.setCourseName("Calculus");
        course.setCredit(4);

        courseRepository.save(course);

        Course1 found = courseRepository.findByCourseId("MATH201");
        assertNotNull(found);
        assertEquals("Calculus", found.getCourseName());
    }

    @Test
    void testDeleteCourse() {
        Course1 course = new Course1();
        course.setCourseId("PHY101");
        course.setCourseName("Physics");
        course.setCredit(3);

        Course1 savedCourse = courseRepository.save(course);

        courseRepository.deleteById(savedCourse.getId());

        Optional<Course1> deleted = courseRepository.findById(savedCourse.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void testUpdateCourse() {
        Course1 course = new Course1();
        course.setCourseId("ENG101");
        course.setCourseName("English");
        course.setCredit(2);

        Course1 savedCourse = courseRepository.save(course);

        savedCourse.setCourseName("Advanced English");
        savedCourse.setCredit(3);
        Course1 updatedCourse = courseRepository.save(savedCourse);

        assertEquals("Advanced English", updatedCourse.getCourseName());
        assertEquals(3, updatedCourse.getCredit());
    }
}