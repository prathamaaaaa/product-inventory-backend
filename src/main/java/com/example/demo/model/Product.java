package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name =  "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    
    private String adminid;
    
    @Column(precision = 10, scale = 0)
    private BigDecimal Price;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SubCategory_Id", nullable = false) // Foreign Key Column
    @JsonBackReference
    private subCategories subCategories;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Category_id", nullable = false)
    @JsonBackReference
    private Categories Categories;
    
    private String details;

    private String storeid;
    public String getStoreid() {
		return storeid;
	}

	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}


    @Column(name = "image_urls", length = 1000) // Store as a comma-separated string
    private String imageUrls = ""; // Default value to prevent null

    
    
    

	
	private boolean active;
    public String getAdminid() {
		return adminid;
	}

    public void setAdminid(String adminid) {  // ✅ Correct method name
        this.adminid = adminid;
    }


	public List<String> getImageUrls() {
        if (imageUrls == null || imageUrls.trim().isEmpty()) {
            return List.of(); // Return an empty list instead of null
        }
        return Arrays.asList(imageUrls.split("\\s*,\\s*"));
    }

    public void setImageUrls(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            this.imageUrls = ""; // Prevent storing null
        } else {
            this.imageUrls = String.join(",", imageUrls);
        }
    }

    public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public subCategories getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(subCategories subCategories) {
        this.subCategories = subCategories;
    }

    public void setPrice(BigDecimal price) {
        Price = price;
    }

    public String getName() {
        return name;
    }

    public subCategories getSubCategory() {
        return subCategories;
    }

    public void setSubCategory(subCategories subCategory) {
        subCategories = subCategory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Categories getCategories() {
        return Categories;
    }

    public void setCategories(Categories categories) {
        Categories = categories;
    }
    
    
//    public void setName(String trim) {
//    }


	

	
}
