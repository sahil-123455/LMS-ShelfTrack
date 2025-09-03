package com.intern.LMS.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.intern.LMS.Entity.Student;
import com.intern.LMS.Repository.StudentRepository;
import com.intern.LMS.Entity.Issue;
import com.intern.LMS.Repository.IssueRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final IssueRepository issueRepository;

    public StudentService(StudentRepository studentRepository, IssueRepository issueRepository) {
        this.studentRepository = studentRepository;
        this.issueRepository = issueRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
    
    public void deleteStudent(Long id) {
        Student studentToDelete = studentRepository.findById(id).orElse(null);
        if (studentToDelete != null) {
            List<Issue> studentIssues = issueRepository.findByStudent(studentToDelete);
            issueRepository.deleteAll(studentIssues);
            studentRepository.delete(studentToDelete);
        }
    }
    
    public long getTotalStudents() {
        return studentRepository.count();
    }
    
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
}