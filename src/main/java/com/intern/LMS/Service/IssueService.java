package com.intern.LMS.Service;

import org.springframework.stereotype.Service;
import com.intern.LMS.Entity.Issue;
import com.intern.LMS.Entity.Student;
import com.intern.LMS.Entity.User;
import com.intern.LMS.Repository.IssueRepository;
import com.intern.LMS.Repository.StudentRepository;
import java.util.List;
import java.time.LocalDate;

@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final StudentService studentService;
    private final StudentRepository studentRepository; 

    public IssueService(IssueRepository issueRepository, StudentService studentService, StudentRepository studentRepository) {
        this.issueRepository = issueRepository;
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    public void saveIssue(Issue issue) {
        issueRepository.save(issue);
    }

    public List<Issue> getAllIssuedBooks() {
        return issueRepository.findAll();
    }
    
    public Issue getIssueById(Long id) {
        return issueRepository.findById(id).orElse(null);
    }
    
    public void deleteIssue(Long id) {
        issueRepository.deleteById(id);
    }
    
    public List<Issue> findIssuedBooksByStudent(Student student) {
        return issueRepository.findByStudentAndIsReturnedFalse(student);
    }
    
    // ✅ CORRECTED: Method to get defaulter list
    public List<Issue> getDefaulterList() {
        // Find books that are not returned AND are overdue (due date is before today)
        return issueRepository.findByIsReturnedFalseAndDueDateBefore(LocalDate.now());
    }
    
    public List<Issue> findIssuedBooksForUser(User user) {
        Student student = studentService.getStudentByEmail(user.getEmail());
        if (student != null) {
            return issueRepository.findByStudentAndIsReturnedFalse(student);
        }
        return null;
    }

    public long getTotalIssuedBooksByUser(User user) {
        Student student = studentService.getStudentByEmail(user.getEmail());
        if (student != null) {
            return issueRepository.countByStudent(student);
        }
        return 0;
    }

    public long getCurrentlyIssuedBooksByUser(User user) {
        Student student = studentService.getStudentByEmail(user.getEmail());
        if (student != null) {
            return issueRepository.countByStudentAndIsReturnedFalse(student);
        }
        return 0;
    }

    public long getReturnedBooksByUser(User user) {
        Student student = studentService.getStudentByEmail(user.getEmail());
        if (student != null) {
            return issueRepository.countByStudentAndIsReturnedTrue(student);
        }
        return 0;
    }
}