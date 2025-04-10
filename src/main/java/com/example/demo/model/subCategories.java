
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subCategories")
public class subCategories{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name; 

    @ManyToOne
    @JoinColumn(name = "Category_id", nullable = false)
    @JsonBackReference
    private Categories Categories; 

    @OneToMany(mappedBy = "subCategories", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> Product;

    
    private String adminid;
    
    private String storeid;
    
    
    
    
    public String getStoreid() {
		return storeid;
	}

	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}


    public String getAdminid() {
		return adminid;
	}

	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}

	public List<Product> getProduct() {
        return Product;
    }

    public void setProduct(List<Product> product) {
        Product = product;
    }


    public Categories getCategories() {
        return Categories;
    }

    public void setCategories(Categories categories) {
        Categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
