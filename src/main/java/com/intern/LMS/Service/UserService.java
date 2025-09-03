package com.intern.LMS.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.intern.LMS.Entity.User;
import com.intern.LMS.Repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // ✅ NEW: Count total users in the database
    public long getTotalUsers() {
        return userRepository.count();
    }

	public Object findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}
}