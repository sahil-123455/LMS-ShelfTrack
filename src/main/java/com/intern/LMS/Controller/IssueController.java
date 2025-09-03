package com.intern.LMS.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // ✅ This import is now included
import com.intern.LMS.Entity.Book;
import com.intern.LMS.Entity.Issue;
import com.intern.LMS.Entity.Student;
import com.intern.LMS.Service.BookService;
import com.intern.LMS.Service.IssueService;
import com.intern.LMS.Service.StudentService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class IssueController {
    private final IssueService issueService;
    private final BookService bookService;
    private final StudentService studentService;

    public IssueController(IssueService issueService, BookService bookService, StudentService studentService) {
        this.issueService = issueService;
        this.bookService = bookService;
        this.studentService = studentService;
    }

    // ✅ Display all issued books
    @GetMapping("/issued-books")
    public String getAllIssuedBooks(Model model) {
        model.addAttribute("issues", issueService.getAllIssuedBooks());
        return "issued-books";
    }

    // ✅ Display form to issue a book
    @GetMapping("/books/issue/{bookId}")
    public String showIssueForm(@PathVariable Long bookId, Model model) {
        Book book = bookService.getBookById(bookId);
        List<Student> students = studentService.getAllStudents();
        
        model.addAttribute("book", book);
        model.addAttribute("students", students);
        model.addAttribute("issue", new Issue());
        return "issue-form";
    }

    // ✅ Handle form submission and save the issue record
    @PostMapping("/books/issue/save/{bookId}")
    public String saveIssue(@PathVariable Long bookId, @ModelAttribute("issue") Issue issue, @RequestParam("studentId") Long studentId) {
        Book book = bookService.getBookById(bookId);
        Student student = studentService.getStudentById(studentId);
        
        if (book != null && student != null) {
            issue.setBook(book);
            issue.setStudent(student);
            issue.setIssueDate(LocalDate.now());
            issue.setDueDate(LocalDate.now().plusDays(15));
            issue.setReturned(false);
            
            // Mark the book as not available
            book.setAvailable(false);
            bookService.saveBook(book);
            
            issueService.saveIssue(issue);
        }
        return "redirect:/issued-books";
    }

    // ✅ Handle book return
    @GetMapping("/books/return/{issueId}")
    public String returnBook(@PathVariable Long issueId, RedirectAttributes redirectAttributes) {
        Issue issue = issueService.getIssueById(issueId);
        if (issue != null) {
            issue.setReturnDate(LocalDate.now());
            issue.setReturned(true);
            
            // Fine calculation
            long fine = 0;
            if (issue.getDueDate() != null && issue.getReturnDate() != null) {
                long daysBetween = ChronoUnit.DAYS.between(issue.getDueDate(), issue.getReturnDate());
                if (daysBetween > 0) {
                    fine = daysBetween * 10;
                    redirectAttributes.addFlashAttribute("fineAmount", fine);
                    redirectAttributes.addFlashAttribute("studentName", issue.getStudent().getName());
                    redirectAttributes.addFlashAttribute("bookTitle", issue.getBook().getTitle());
                }
            }
            
            // Mark the book as available again
            Book book = issue.getBook();
            book.setAvailable(true);
            bookService.saveBook(book);
            
            issueService.saveIssue(issue);
        }
        return "redirect:/issued-books";
    }
    
    // ✅ Display list of issued books for a specific student
    @GetMapping("/students/{studentId}/issued-books")
    public String getIssuedBooksForStudent(@PathVariable Long studentId, Model model) {
        Student student = studentService.getStudentById(studentId);
        if (student != null) {
            model.addAttribute("issuedBooks", issueService.findIssuedBooksByStudent(student));
        }
        return "student-issued-books";
    }
    
    // ✅ Display Defaulter List
    @GetMapping("/defaulters")
    public String showDefaulterList(Model model) {
        List<Issue> defaulterList = issueService.getDefaulterList();
        model.addAttribute("defaulters", defaulterList);
        return "defaulter-list";
    }
}