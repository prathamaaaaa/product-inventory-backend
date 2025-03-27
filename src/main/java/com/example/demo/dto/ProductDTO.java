package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductDTO {
    private int id;
    private String name;
    private BigDecimal price;
    private String categoryName;
    private String subCategoryName;
    private List<String> imageUrls;
    private String details;
    private boolean active;
    private String adminid;
    private String storeid;
    // ✅ Fix constructor order to match how it's called in the controller
    public ProductDTO(int id, String name, BigDecimal price, String categoryName, 
                      String subCategoryName, List<String> imageUrls, String details,boolean active, String adminid , String storeid ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.imageUrls = imageUrls;
        this.details = details;
        this.active = active;
        this.adminid = adminid;
        this.storeid=storeid;
    }

    // ✅ Standard Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getCategoryName() { return categoryName; }
    public String getSubCategoryName() { return subCategoryName; }
    public List<String> getImageUrls() { return imageUrls; }
    public String getDetails() { return details; }
    public boolean isActive() { return active; }
    public String getAdminid() {return adminid ;}
    public String getStoreid() {return storeid;}
}
