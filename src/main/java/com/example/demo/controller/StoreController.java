package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.model.Store;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreRepository;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend requests
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addStore(@RequestBody Store store) {
        if (store.getName() == null || store.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Store name cannot be empty");
        }
        storeRepository.save(store);
        return ResponseEntity.ok("Store created successfully");
    }
    
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Store>> getStoresByAdmin(@PathVariable String adminId) {
        List<Store> stores = storeRepository.findByAdminid(adminId);
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable int id) {
        Optional<Store> store = storeRepository.findById(id);
        return store.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build());
    }
    
    @GetMapping("/product/{storeId}")
    public ResponseEntity<List<ProductDTO>> getProductsByStore(@PathVariable String storeId) {
        System.out.println(" storeId: " + storeId);

        List<Product> products = productRepository.findByStoreid(storeId);

        List<ProductDTO> productDTOs = products.stream().map(product -> new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategories() != null ? product.getCategories().getName() : "N/A",
                product.getSubCategories() != null ? product.getSubCategories().getName() : "N/A",
                product.getImageUrls(),
                product.getDetails(),
                product.isActive(),
                product.getAdminid(),
                product.getStoreid()
        )).toList();

        return ResponseEntity.ok(productDTOs);
    }

    
}
