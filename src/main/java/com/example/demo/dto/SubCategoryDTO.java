package com.example.demo.dto;

import com.example.demo.model.subCategories;

public class SubCategoryDTO {
    private int id;
    private String name;
    private int categoryId;
    private String categoryName;

    public SubCategoryDTO(subCategories subCategory) {
        this.id = subCategory.getId();
        this.name = subCategory.getName();
        this.categoryId = subCategory.getCategories() != null ? subCategory.getCategories().getId() : 0;
        this.categoryName = subCategory.getCategories() != null ? subCategory.getCategories().getName() : "N/A";
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
}
