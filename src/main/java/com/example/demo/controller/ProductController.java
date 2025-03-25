package com.example.demo.controller;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.SubCategoryDTO;
import com.example.demo.model.Categories;
import com.example.demo.model.Product;
import com.example.demo.model.subCategories;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    
    
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();

        List<ProductDTO> productDTOs = productRepository.findAll().stream().map(product -> 
            new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategories() != null ? product.getCategories().getName() : "N/A",
                product.getSubCategories() != null ? product.getSubCategories().getName() : "N/A",
                product.getImageUrls() ,
                product.getDetails(),
                product.isActive(),
                product.getAdminid()            
                )
            ).collect(Collectors.toList());
        List<CategoryDTO> categories = categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
        List<SubCategoryDTO> subCategories = subCategoryRepository.findAll().stream().map(SubCategoryDTO::new).toList();
        
       
        response.put("categories", categories);
        response.put("subCategories", subCategories);

        response.put("products", productDTOs);
        return ResponseEntity.ok(response);
    }


    
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        Product product = productRepository.findById(id); 
        ProductDTO productDTO = new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getCategories() != null ? product.getCategories().getName() : "N/A",
            product.getSubCategories() != null ? product.getSubCategories().getName() : "N/A",
            product.getImageUrls(),
            product.getDetails(),
            product.isActive(),
            product.getAdminid()
        );

        
        
        return ResponseEntity.ok(productDTO);
    }


}
