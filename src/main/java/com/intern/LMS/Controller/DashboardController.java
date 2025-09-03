package com.intern.LMS.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

import com.intern.LMS.Entity.User;
import com.intern.LMS.Service.BookService;
import com.intern.LMS.Service.UserService;
import com.intern.LMS.Service.StudentService;

@Controller
public class DashboardController {
    private final BookService bookService;
    private final UserService userService;
    private final StudentService studentService;

    public DashboardController(BookService bookService, UserService userService, StudentService studentService) {
        this.bookService = bookService;
        this.userService = userService;
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Fetch dynamic data from services
        model.addAttribute("totalBooks", bookService.getTotalBooks());
        model.addAttribute("issuedBooks", bookService.getIssuedBooksCount());
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("totalStudents", studentService.getTotalStudents());

        // Pass the user object to the dashboard page
        model.addAttribute("user", loggedInUser);

        return "dashboard";
    }
}