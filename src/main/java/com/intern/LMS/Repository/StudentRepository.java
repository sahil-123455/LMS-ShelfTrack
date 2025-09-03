package com.intern.LMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.intern.LMS.Entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Student findByEmail(String email);
}