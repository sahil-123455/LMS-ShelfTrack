package com.intern.LMS.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.intern.LMS.Entity.User;
import com.intern.LMS.Service.UserService;
import com.intern.LMS.Service.IssueService;
import com.intern.LMS.Entity.Issue;

@Controller
public class UserController {
    private final UserService userService;
    private final IssueService issueService;

    public UserController(UserService userService, IssueService issueService) {
        this.userService = userService;
        this.issueService = issueService;
    }

    @GetMapping("/")
    public String defaultPage() {
        return "redirect:/register";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        user.setRole("USER");
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user, Model model, HttpSession session) {
        User existingUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (existingUser != null) {
            session.setAttribute("user", existingUser);
            
            if ("ADMIN".equals(existingUser.getRole())) {
                return "redirect:/dashboard";
            } else {
                return "redirect:/student-dashboard";
            }
        } else {
            model.addAttribute("error", "Invalid email or password!");
            return "login";
        }
    }
    
    @GetMapping("/student-dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        
        // ✅ NEW: Fetch analytics data for the student
        long totalIssued = issueService.getTotalIssuedBooksByUser(loggedInUser);
        long currentlyIssued = issueService.getCurrentlyIssuedBooksByUser(loggedInUser);
        long returnedBooks = issueService.getReturnedBooksByUser(loggedInUser);

        List<Issue> issuedBooks = issueService.findIssuedBooksForUser(loggedInUser);
        
        model.addAttribute("totalIssued", totalIssued);
        model.addAttribute("currentlyIssued", currentlyIssued);
        model.addAttribute("returnedBooks", returnedBooks);
        model.addAttribute("issuedBooks", issuedBooks);
        model.addAttribute("user", loggedInUser);
        
        return "student-dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}