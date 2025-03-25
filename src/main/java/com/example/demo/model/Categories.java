package com.example.demo.model;

import jakarta.persistence.*;

import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Categories")
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "Categories", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference 
    private List<subCategories> subcategories;
    
    
    @OneToMany(mappedBy = "Categories", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> product; // One product can have multiple categories

    public int getId() {
        return id;
    }

    public List<subCategories> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<subCategories> subcategories) {
        this.subcategories = subcategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }
}
