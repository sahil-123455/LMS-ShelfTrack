package com.intern.LMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.intern.LMS.Entity.Issue;
import java.util.List;
import com.intern.LMS.Entity.Student;
import com.intern.LMS.Entity.Book;
import java.time.LocalDate;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByStudentAndIsReturnedFalse(Student student);
    List<Issue> findByStudent(Student student);
    long countByStudent(Student student);
    long countByStudentAndIsReturnedFalse(Student student);
    long countByStudentAndIsReturnedTrue(Student student);
    List<Issue> findByBook(Book book);

    // ✅ CORRECTED: Now finds books that are not returned AND are overdue
    List<Issue> findByIsReturnedFalseAndDueDateBefore(LocalDate date);
}