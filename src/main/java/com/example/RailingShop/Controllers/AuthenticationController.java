package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;
//--- Може би ще трябва да се направи нещо което да провери всеки път дали потребителя е логнат и ако е да има достъп до страниците в противен случай да я препраща в /login За да се логне. Това е големият проблем.

@Controller
@RequestMapping("/shop")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    public String getIndexMenu(){
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

//    @PostMapping("/login")
//    public String processLoginForm(@ModelAttribute("user") User user, HttpSession session, Model model) {
//        Optional<User> authenticatedUser = authService.authenticateUser(user.getUsername(), user.getPassword());
//        System.out.println("Controller user login check");
//        if (authenticatedUser.isPresent()) {
//            session.setAttribute("user", authenticatedUser.get());
//            return "redirect:/shop/home";
//        } else {
//            model.addAttribute("error", "Invalid username or password");
//            return "login";
//        }
//    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user,Model model, RedirectAttributes redirectAttributes) {
         User userToBlockChain = userRepository.findByUsername(user.getUsername());
        if (userToBlockChain == null || !passwordEncoder.matches(user.getPassword(), userToBlockChain.getPassword())) {
            redirectAttributes.addFlashAttribute("wrongCredentials", "wrong credentials");
            return "/shop/login";
        }
        return "redirect:/shop/home";
    }



    @GetMapping("/home")
    public String showHome(Model model,HttpServletRequest request){
//        User user=(User) session.getAttribute("user");
//        try {
//            user=authService.takeUserSession(session);
//        }catch (RuntimeException e){
//            redirectAttributes.addFlashAttribute("message",e.getMessage());
//        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
        else {
            model.addAttribute("user",user);
            model.addAttribute("userRole", user.getRole().toString());

            Locale currentLocale=request.getLocale();
            String countryCode=currentLocale.getCountry();
            String countryName=currentLocale.getDisplayCountry();

            String langCode=currentLocale.getLanguage();
            String langName=currentLocale.getDisplayLanguage();

            System.out.println(countryCode+": "+countryName);
            System.out.println(langCode+ ": "+langName);

//            System.out.println("=========");
//            String[] languages=Locale.getISOLanguages();
//            for (String language:languages){
//                Locale locale=new Locale(language);
//                System.out.println(language+": "+locale.getDisplayLanguage());
//            }

            return "home";
        }
    }

    @GetMapping("/logout")
    public String logoutButton(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
        session.removeAttribute("user");
        System.out.println("remove session");
        return "redirect:/shop/login";
    }
}

//    @PostMapping("/login")
//    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
//        User user = userRepository.findByUsername(username);
//        if (user == null || passwordEncoder.matches(password, user.getPassword())) {
//            model.addAttribute("error", "User not found.");
//            return "login";
//        }
//        return "redirect:/shop/home";
//    }


    //    @PostMapping("/login")
//    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
//        User user = userRepository.findByUsername(username);
//        if (user == null || passwordEncoder.matches(password, user.getPassword())) {
//            model.addAttribute("error", "User not found.");
//            return "login";
//        }
//        return "redirect:/index";
//    }
//    @GetMapping("/logout")
//    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
//        this.logoutHandler.logout(request, response, authentication);//.doLogout(request, response, authentication);
//        return "redirect:/login";
//    }
