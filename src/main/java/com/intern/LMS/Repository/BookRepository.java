package com.intern.LMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.intern.LMS.Entity.Book;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    
    // ✅ NEW: Count of books that are not available (issued)
    long countByAvailableFalse();
}