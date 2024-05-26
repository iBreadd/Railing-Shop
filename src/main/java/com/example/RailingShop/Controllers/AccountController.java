package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//---- ИМА address_up.html обаче не сработи (препраща ме в /shop/all (Проблема е че след като се логна нищо не проверява дали съм логнат), вписано е в SecurityConfig

@Controller
@RequestMapping("/shop")
public class AccountController {

    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/accountDetails")
    public String accountDetails(Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "You are not logged in");
            return "redirect:/shop/login";
        } else {
            model.addAttribute("user", user);
            return "address_up";
        }
    }

    @PostMapping("/updateAddress")
    public String saveUserForm(@Valid User userForm, BindingResult bindingResult, Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "redirect:/shop/login";
        }
        if (bindingResult.hasErrors()) {
            return "address_up";
        }

        // Update the existing user with the new information
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        user.setPhone(userForm.getPhone());
        user.setCity(userForm.getCity());
        user.setPostalCode(userForm.getPostalCode());
        user.setAddress(userForm.getAddress());

        userService.updateUserInformation(user);

        User updatedUser = userService.findById(user.getId());
        session.setAttribute("user", updatedUser);
        model.addAttribute("message", "Your information has been updated successfully!!!");
        return "redirect:/shop/all";
    }
}


