package com.project.idea.repository;

import com.project.idea.model.Course1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course1, Long> {
}
