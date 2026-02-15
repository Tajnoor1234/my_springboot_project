package com.project.idea.repository;

import com.project.idea.model.Course1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course1, Long> {
    // Add this method for finding courses by courseId
    Course1 findByCourseId(String courseId);
}