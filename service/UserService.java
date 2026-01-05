package com.banking.service;

import com.banking.entity.User;
import com.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
    @Autowired
    private UserRepository repo;

    public User registerUser(User user) {
        return repo.save(user);
    }

    public User findByEmail(String email) {
        return repo.findByEmail(email);
    }
   
    public boolean updatePassword(String email, String encryptedPassword){
        User user = repo.findByEmail(email);
        if(user == null) return false;

        user.setPassword(encryptedPassword);
        repo.save(user);
        return true;
    }
    

    public void updateProfile(String oldEmail, String password, String newEmail, String phone, String address) {
        User user = repo.findByEmail(oldEmail);

        // Encrypt here (only place where encryption should happen)
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        user.setEmail(newEmail);
        user.setPhone(phone);
        user.setAddress(address);

        repo.save(user);
    }


}
