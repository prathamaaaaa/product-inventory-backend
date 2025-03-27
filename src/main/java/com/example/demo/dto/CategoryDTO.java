package com.example.demo.dto;

import com.example.demo.model.Categories;

public class CategoryDTO {
    private int id;
    private String name;
    private String adminid;
    private String storeid;

    public CategoryDTO(Categories category) {
        this.id = category.getId();
        this.name = category.getName();
        
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
}
