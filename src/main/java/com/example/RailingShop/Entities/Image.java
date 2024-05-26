//package com.example.RailingShop.Entities;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.util.UUID;
//
//@Entity
//@Data
//@Table(name = "images")
//public class Image {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(name = "id")
//    private UUID id;
//
//    @Column(name = "name_image")
//    private String name;
//
//    @Column(name = "url_image")
//    private String url;
//
//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//}