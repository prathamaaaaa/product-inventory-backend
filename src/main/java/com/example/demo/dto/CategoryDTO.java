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
        this.adminid=category.getAdminid();
        this.storeid = category.getStoreid();
        
    }
    
    public int getId() { return id; }
    public String getName() { return name; }

	public String getAdminid() {
		return adminid;
	}

	public String getStoreid() {
		return storeid;
	}
    
}
