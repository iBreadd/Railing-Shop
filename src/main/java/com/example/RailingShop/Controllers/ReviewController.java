package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.Review;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.ProductRepository;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shop")
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/products/{productId}/reviews")
    public String showReviews(@PathVariable Long productId, Model model) {
        Product product = productRepository.findById(productId).orElseThrow();
        model.addAttribute("product", product);
        model.addAttribute("reviews", product.getReviews());
        return "reviews";
    }

    @GetMapping("/products/{productId}/reviews/new")
    public String showNewReviewForm(@PathVariable Long productId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return "redirect:/shop/login";
        }

        Review review = new Review();
        review.setProduct(productRepository.findById(productId).orElseThrow());
        review.setUser(user);
        model.addAttribute("review", review);
        return "new-review";
    }

    @PostMapping("/products/{productId}/reviews")
    public String saveReview(@PathVariable Long productId, @ModelAttribute Review review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return "redirect:/shop/login";
        }

        review.setUser(user);
        review.setProduct(productRepository.findById(productId).orElseThrow());
        reviewService.saveReview(review);
        return "redirect:/shop/products/" + productId + "/reviews";
    }
}
