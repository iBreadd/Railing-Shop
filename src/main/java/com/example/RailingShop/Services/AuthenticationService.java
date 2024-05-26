package com.example.RailingShop.Services;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;

    public Optional<User> authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public User takeUserSession(HttpSession session) {
        String id=null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal=authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                id=((UserDetails)principal).getUsername();
            }
        }
        User user=userRepository.findById(Long.valueOf(id)).orElse(null);
        if (user==null){
            throw new RuntimeException("Не е намерен потребител");
        }
        session.setAttribute("user",user);
        return user;
    }
}


