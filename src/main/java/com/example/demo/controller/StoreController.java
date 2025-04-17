package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.SubCategoryDTO;
import com.example.demo.model.Admin;
import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.model.Store;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.SubCategoryRepository;
import com.example.demo.service.CsvService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import io.opencensus.resource.Resource;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend requests
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
	private CsvService csvService;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @GetMapping("/download-csv/{storeId}")
    public ResponseEntity<ByteArrayResource> downloadProductsCSV(@PathVariable String storeId) {
        List<Product> products = productRepository.findByStoreid(storeId);

        if (products.isEmpty()) {
            return ResponseEntity.badRequest().body(new ByteArrayResource("No products available.".getBytes()));
        }

        byte[] csvData = csvService.generateCSV(products);
        ByteArrayResource resource = new ByteArrayResource(csvData);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=store_" + storeId + "_products.csv")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    
    @DeleteMapping("/delete/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable String storeId) {
        try {
            System.out.println("Deleting store ID: " + storeId);

            List<Product> products = productRepository.findByStoreid(String.valueOf(storeId));

            if (!products.isEmpty()) {
                for (Product product : products) {
                    int productId = product.getId();

                    Optional<Cart> cartItemOpt = cartRepository.findByProductid(productId);
                    cartItemOpt.ifPresent(cartItem -> {
                        cartRepository.delete(cartItem);
                        System.out.println("Deleted cart item for product ID: " + productId);
                    });
                }

                productRepository.deleteAll(products);
                System.out.println("Deleted all products of storeId: " + storeId);
            }

            storeRepository.deleteById(Integer.parseInt(storeId));
            System.out.println("Deleted store ID: " + storeId);

            return ResponseEntity.ok("Store, associated products, and related cart items deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting store: " + e.getMessage());
        }
    }





//
//    @PostMapping("/add")
//    public ResponseEntity<String> addStore(@RequestBody Store store) {
//    	System.out.println(store);
//        if (store.getName() == null || store.getName().trim().isEmpty()) {
//            return ResponseEntity.badRequest().body("Store name cannot be empty");
//        }
//        storeRepository.save(store);
//        return ResponseEntity.ok("Store created successfully");
//    }
    
    @PostMapping("/add")
    public ResponseEntity<?> createStore(@RequestBody Map<String, Object> request) {
        try {
            String adminId = request.get("adminid").toString();
//            String admin = adminRepository.findById(adminId);

        
            ObjectMapper objectMapper = new ObjectMapper(); 


            // Parse `name` from JSON string
            String nameJsonStr = request.get("name").toString();
            JsonNode nameJson = objectMapper.readTree(nameJsonStr);

            Store store = new Store();
            store.setAdminid(adminId);
            store.setName(nameJsonStr); // Save JSON

            storeRepository.save(store);
            return ResponseEntity.ok("Store created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
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
    public ResponseEntity<Map<String, Object>> getProductsByStore(@PathVariable String storeId) {
        System.out.println(" storeId: " + storeId);
        Map<String, Object> response = new HashMap<>();

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
        
        List<CategoryDTO> categories = categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
        List<SubCategoryDTO> subCategories = subCategoryRepository.findAll().stream().map(SubCategoryDTO::new).toList();
        
       
        response.put("categories", categories);
        response.put("subCategories", subCategories);

        response.put("products", productDTOs);
        
        return ResponseEntity.ok(response);
    }

    
}
