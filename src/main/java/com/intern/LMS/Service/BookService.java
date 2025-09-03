package com.intern.LMS.Service;

import org.springframework.stereotype.Service;
import com.intern.LMS.Entity.Book;
import com.intern.LMS.Repository.BookRepository;
import java.util.List;
import com.intern.LMS.Entity.Issue;
import com.intern.LMS.Repository.IssueRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final IssueRepository issueRepository;

    public BookService(BookRepository bookRepository, IssueRepository issueRepository) {
        this.bookRepository = bookRepository;
        this.issueRepository = issueRepository;
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }
    
    // ✅ THIS FIXES THE DELETE ERROR:
    public void deleteBook(Long id) {
        Book bookToDelete = bookRepository.findById(id).orElse(null);
        if (bookToDelete != null) {
            // Step 1: Find and delete all issues for this book
            List<Issue> bookIssues = issueRepository.findByBook(bookToDelete);
            issueRepository.deleteAll(bookIssues);

            // Step 2: Now, delete the book
            bookRepository.delete(bookToDelete);
        }
    }
    
    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }
    
    public long getTotalBooks() {
        return bookRepository.count();
    }
    
    public long getIssuedBooksCount() {
        return bookRepository.countByAvailableFalse();
    }
}