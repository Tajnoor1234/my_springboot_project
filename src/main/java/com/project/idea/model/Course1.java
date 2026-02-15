package com.project.idea.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "courses1")
@Data
public class Course1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseId;
    private String courseName;
    private int credit;
}
