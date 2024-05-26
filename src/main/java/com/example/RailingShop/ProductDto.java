package com.example.RailingShop;

import com.example.RailingShop.Enums.ProductColor;
import com.example.RailingShop.Enums.ProductMaterial;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class ProductDto {
    @Column(name = "name")
    @NotBlank(message = "Името на продукта не може да бъде празно!")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    @NotNull(message = "Цената не може да бъде нула!")
    @Min(value = 0,message = "Цената трябва да е по-голяма от 0.00")
    private BigDecimal price;

    @Column(name = "quantity")
    @NotNull(message = "Количеството не може да бъде нула!")
    @Min(value = 0, message = "Количеството трябва да е повече или равно на 1")
    private int quantity;

    @Column
    @NotNull
    private boolean available;
    @Column(name = "color")
    @NotNull(message = "Цвета не може да бъде празна!")
    @Enumerated(EnumType.STRING)
    private ProductColor color;
    @Column(name = "material")
    @NotNull(message = "Материала не може да бъде празна!")
    @Enumerated(EnumType.STRING)
    private ProductMaterial material;
    @Column
    private MultipartFile imageFile;
    @Column(name = "manufacturer")
    @NotBlank(message = "Производителя не може да бъде празна!")
    private String manufacturer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ProductColor getColor() {
        return color;
    }

    public void setColor(ProductColor color) {
        this.color = color;
    }

    public ProductMaterial getMaterial() {
        return material;
    }

    public void setMaterial(ProductMaterial material) {
        this.material = material;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
