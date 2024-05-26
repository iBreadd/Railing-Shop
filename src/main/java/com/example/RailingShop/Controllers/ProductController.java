package com.example.RailingShop.Controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.RailingShop.Entities.OrderProduct;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.ProductColor;
import com.example.RailingShop.Enums.ProductMaterial;
import com.example.RailingShop.ProductDto;
import com.example.RailingShop.Repositories.ProductRepository;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.ProductService;
import com.example.RailingShop.service.CloudinaryService;
import com.example.RailingShop.service.ImageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/shop")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    private ImageService imageService;
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
        Product product = new Product();
        model.addAttribute("product", new Product());
        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(Product product, MultipartFile imageFile){
        if (!imageFile.isEmpty()) {
            try {
                String imageUrl = imageService.uploadImage(imageFile);
                product.setImageUrl(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productRepository.save(product);
        return "redirect:/shop/products";
    }

//    @GetMapping(value = "/{productId}/image")
//    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
//        Optional<Product> productOptional = productService.getProduct(productId);
//        if (productOptional.isPresent()) {
//            Product product = productOptional.get();
//            byte[] imageBytes = java.util.Base64.getDecoder().decode(product.getImageUrl());
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG);
//            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(new byte[0], HttpStatus.NOT_FOUND);
//        }
//    }
//    @PostMapping("/addProduct")
//    public String saveProduct(@RequestParam("file") MultipartFile file,
//                              @RequestParam("pname") String name,
//                              @RequestParam("price") BigDecimal price,
//                              @RequestParam("desc") String description,
//                              @RequestParam("quantity") int quantity,
//                              @RequestParam("available") boolean available,
//                              @RequestParam("color") ProductColor color,
//                              @RequestParam("material") ProductMaterial material,
//                              @RequestParam("manufacturer") String manufacturer)
//    {
//        productService.saveProductToDB(file, name, price,description,quantity,available,color,material,manufacturer);
//        return "redirect:/shop/products";
//    }

//    @PostMapping("/addProduct")
//    public String addProduct(@ModelAttribute @Valid ProductDto productDto,
//                             BindingResult result, RedirectAttributes redirectAttributes) {

//        if(productDto.getImageFile().isEmpty()){
//            result.addError(new FieldError("product","imageFile","The image file is required"));
//        }

//        if(result.hasErrors()){
//            return "addProduct";
//        }
//        MultipartFile image = productDto.getImageFile();
//        String originalFileName = image.getOriginalFilename();
//        String storageFileName = System.currentTimeMillis() + "_" + originalFileName;
//
//        try {
//            String uploadDir = "public/img/";
//            Path uploadPath = Paths.get(uploadDir);
//
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            try (InputStream inputStream = image.getInputStream()) {
//                Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
//            }
//        } catch (IOException ex) {
//            result.addError(new FieldError("productDto", "imageFile", "Could not save image file: " + ex.getMessage()));
//            return "addProduct";
//        }
//
//        Product product = new Product();
//        product.setName(productDto.getName());
//        product.setAvailable(productDto.isAvailable());
//        product.setColor(productDto.getColor());
//        product.setDescription(productDto.getDescription());
//        product.setManufacturer(productDto.getManufacturer());
//        product.setMaterial(productDto.getMaterial());
//        product.setQuantity(productDto.getQuantity());
//        product.setPrice(productDto.getPrice());
//        product.setImageUrl(storageFileName);
//
//        productRepository.save(product);
//
//        return "redirect:/shop/products";
//        ModelAndView modelAndView = new ModelAndView();
//
//        if (result.hasErrors()) {
//            modelAndView.setViewName("/shop/addProduct");
//            return modelAndView;
//        }else {//
//            productService.addProduct(product);//
//            modelAndView.setViewName("redirect:/shop/products");//
//        }//
//        return modelAndView;//
//
//
//        if (!imageFile.isEmpty()) {
//            try {
//                String fileName = UUID.randomUUID().toString() + "-" + imageFile.getOriginalFilename();
//                Path path = Paths.get(UPLOAD_DIR + fileName);
//                Files.createDirectories(path.getParent());
//
//                Files.write(path, imageFile.getBytes());
//
//                product.setImageUrl("/" + UPLOAD_DIR + fileName);
//            } catch (IOException e) {
//                result.rejectValue("imageUrl", "error.product", "Неуспешно качване на файла");
//                modelAndView.setViewName("addProduct");
//                return modelAndView;
//            }
//        }
//
//        productService.addProduct(product);
//        modelAndView.setViewName("redirect:/shop/products");
//        return modelAndView;
//    }

    @GetMapping("/products")
    public String getProducts(@RequestParam(defaultValue = "name") String sortBy,
                              @RequestParam(defaultValue = "asc") String sortDirection,
                              Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
        List<Product> products = productService.getSortedProducts(sortBy, sortDirection);
        model.addAttribute("products", products);
        model.addAttribute("sortDirection", sortDirection);
        return "products";
    }

    @PostMapping("/delete/{productId}")
    private ModelAndView deleteProduct(@PathVariable(name = "productId") Long productId) {
        productService.deleteProduct(productId);
        return new ModelAndView("redirect:/shop/products");
    }

    @GetMapping("/editProduct/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "redirect:/shop/login";
        }
        model.addAttribute("product", productService.getProductById(id));
        return "editProduct";
    }

    @PostMapping("/editProduct/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        if (existingProduct == null) {
            return "redirect:/shop/products";
        }

        // Актуализация на данните на продукта
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setManufacturer(product.getManufacturer());
        existingProduct.setAvailable(product.isAvailable());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setMaterial(product.getMaterial());
        existingProduct.setColor(product.getColor());
        existingProduct.setPrice(product.getPrice());
        // Добавете останалите полета, които трябва да бъдат актуализирани

        // Проверка дали е подаден файл за качване на изображение
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = imageService.uploadImage(imageFile); // Качване на новото изображение
                existingProduct.setImageUrl(imageUrl); // Актуализиране на URL на изображението
            } catch (IOException e) {
                e.printStackTrace(); // Обработка на грешка при качването на изображението
            }
        }

        // Запазване на актуализирания продукт в репозитория
        productRepository.save(existingProduct);

        return "redirect:/shop/products"; // Изберете подходящо URL за пренасочване
    }



    @PostMapping("/products")
    public String searchProducts(
            @RequestParam(name = "searchById", required = false) Long searchById,
            @RequestParam(name = "searchByName", required = false) String searchByName,
            @RequestParam(name = "searchByQuantity", required = false) Integer searchByQuantity,
            @RequestParam(name = "price-min", required = false) BigDecimal priceMin,
            @RequestParam(name = "price-max", required = false) BigDecimal priceMax,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
        if (priceMin != null) {
            priceMin = priceMin.setScale(2, RoundingMode.HALF_UP);
        }
        if (priceMax != null) {
            priceMax = priceMax.setScale(2, RoundingMode.HALF_UP);
        }
        List<Product> products = productService.searchProducts(searchById, searchByName, searchByQuantity,priceMin, priceMax);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/all")
    public String showShop(Model model, @RequestParam(required = false)String keyword, @RequestParam(defaultValue = "name") String sortBy,
                           @RequestParam(defaultValue = "false") boolean inStockOnly,
                           @RequestParam(defaultValue = "0") double minRating,
                           @RequestParam(required = false) BigDecimal minPrice,
                           @RequestParam(required = false) BigDecimal maxPrice,
                           @RequestParam(required = false) ProductColor color,
                           @RequestParam(required = false) ProductMaterial material,
                           @RequestParam(required = false) String manufacturer, OrderProduct orderProduct) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
//            User user = (User) session.getAttribute("user");
        List<Product> products = productService.getFilteredProducts(keyword, sortBy, inStockOnly, minRating, minPrice, maxPrice, color, material, manufacturer);
            model.addAttribute("products", products);
            model.addAttribute("user", user);
            model.addAttribute("items", orderProduct.getQuantity());
            return "all";
    }
}

