package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.Cart;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    UserRepository userRepository;


    @GetMapping("/add")
    public String addOrder(Model model, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes, Principal p) {
//        User user = (User) session.getAttribute("user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "You are not logged in");
            return "redirect:/cart";
        }else{
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getOrderProducts() == null || cart.getOrderProducts().isEmpty()) {
            cart = new Cart();
            redirectAttributes.addFlashAttribute("message", "You have to add products!!!");
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cart.getOrderProducts());

        return "addOrder";
        }
    }

    @PostMapping("/addOrder")
    public String addOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getOrderProducts() == null || cart.getOrderProducts().isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "ShoppingCart is empty");
            return "redirect:/cart";
        }

        try {
            orderService.saveOrder(cart, session);
            session.removeAttribute("cart");
            redirectAttributes.addFlashAttribute("message", "Order has been saved!");
            return "redirect:/shop/all";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/cart";
        }
    }
}


