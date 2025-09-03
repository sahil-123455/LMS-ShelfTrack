package com.intern.LMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.intern.LMS.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
}
