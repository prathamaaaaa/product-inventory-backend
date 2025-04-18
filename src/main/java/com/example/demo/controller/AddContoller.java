package com.example.demo.controller;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CartDTO;
import com.example.demo.model.Admin;
import com.example.demo.model.Cart;
import com.example.demo.model.Categories;
import com.example.demo.model.Product;
import com.example.demo.model.subCategories;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SubCategoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api/products")
public class AddContoller {
	
	    @Autowired
	    private ProductRepository productRepository;
	    @Autowired
	    private CategoryRepository categoryRepository;
	    @Autowired
	    private SubCategoryRepository subCategoryRepository;
	    
	    @Autowired
	    private AdminRepository adminRepository;
	    
	    @Autowired
	    private CartRepository cartRepository;
	    
	    @DeleteMapping("/cart/{userid}/{productid}")
	    public ResponseEntity<String> deleteCartItemByUserAndProduct(@PathVariable int userid, @PathVariable int productid) {
	        try {
	            Cart cartItem = cartRepository.findByUseridAndProductid(userid, productid);
	            if (cartItem != null) {
	                cartRepository.delete(cartItem);
	                return ResponseEntity.ok("Cart item deleted successfully");
	            } else {
	                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Cart item not found for given user and product ID");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error deleting cart item: " + e.getMessage());
	        }
	    }

	    @PostMapping("/cart")
	    public ResponseEntity<String> addToCart(@RequestBody Map<String, Object> cart) {
	        System.out.println(cart);
	    	try {
	            if (!cart.containsKey("productid") || !cart.containsKey("quantity")) {
	                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Missing product ID or quantity");
	            }

	            int productid = (int) cart.get("productid");
	            int quantity = (int) cart.get("quantity");
	            int userid = (int) cart.get("userid");

	            String productname = cart.get("productname").toString();
	            ObjectMapper objectMapper = new ObjectMapper();
	            Map<String, String> productMap = objectMapper.readValue(productname, Map.class);

	            
	            System.out.println("English Name: " + productMap.get("en")); 
	            String englishname = productMap.get("en");
	            System.out.println(englishname);
	            Cart existingCartItem = cartRepository.findByUseridAndProductid(userid, productid);

	            System.out.println(existingCartItem);
	            
	            if (existingCartItem != null) {
	                existingCartItem.setQuantity(quantity);
	                cartRepository.save(existingCartItem);
	                return ResponseEntity.ok("Cart item quantity updated");
	            }  else {
	                Cart newCartItem = new Cart();
	                newCartItem.setProductid(productid);
	                newCartItem.setQuantity(quantity);
	                newCartItem.setProductname(englishname);
	                newCartItem.setUserid(userid);

	                cartRepository.save(newCartItem);
	                return ResponseEntity.ok("Cart item added");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Invalid cart data");
	        }
	    }

	    

	    @PostMapping("/cart/bulk")
	    public ResponseEntity<String> addLocalCartToDatabase(@RequestBody Map<String, Object> data) {
	    	System.out.println(data);
	        try {
	            System.out.println("Received cart data: " + data);

	            Integer userId = (data.get("userid") instanceof Number) ? ((Number) data.get("userid")).intValue() : null;
	            
	            if (userId == null) {
	                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Invalid user ID");
	            }

	            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) data.get("cartItems");
	            if (cartItems == null || cartItems.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Cart items are missing or empty");
	            }

	            for (Map<String, Object> item : cartItems) {
	                System.out.println("Processing cart item: " + item);

	                Integer productid = (item.get("productid") instanceof Number) ? ((Number) item.get("productid")).intValue() : null;
	                String productname = (item.get("productname") instanceof String) ? (String) item.get("productname") : null;
	                Integer quantity = (item.get("quantity") instanceof Number) ? ((Number) item.get("quantity")).intValue() : null;
		           
	                
	                
	                ObjectMapper objectMapper = new ObjectMapper();
		            Map<String, String> productMap = objectMapper.readValue(productname, Map.class);
		            System.out.println("English Name: " + productMap.get("en")); 
		            String englishname = productMap.get("en");
		            System.out.println(englishname);
		        
	                if (productid == null || productname == null || quantity == null) {
	                    System.out.println("Skipping invalid cart item: " + item);
	                    continue;
	                }

	                Cart existingCartItem = cartRepository.findByUseridAndProductid(userId, productid);
	                if (existingCartItem != null) {
//	                    existingCartItem.setQuantity(existingCartItem.getQuantity() );
	                    existingCartItem.setQuantity(existingCartItem.getQuantity()+quantity);
		               
	                    cartRepository.save(existingCartItem);
	                } else {
	                    Cart newCartItem = new Cart();
	                    newCartItem.setUserid(userId);
	                    newCartItem.setProductid(productid);
	                    newCartItem.setProductname(englishname);
	                    newCartItem.setQuantity(quantity);
	                    cartRepository.save(newCartItem);
	                }
	            }
	            return ResponseEntity.ok("Local cart items synced with database.");
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Failed to sync local cart to database.");
	        }
	    }
	    
@PostMapping("/upload-csv")
@CrossOrigin(origins = "http://localhost:5173")
public ResponseEntity<String> uploadCSV(@RequestBody List<Map<String, Object>> productList) {
    try {
        for (Map<String, Object> productData : productList) {
            System.out.println("Received Product Data: " + productData); 

            String name = productData.getOrDefault("name", "").toString().trim();
            String details = productData.getOrDefault("details", "").toString().trim();
            String priceStr = productData.get("price") != null ? productData.get("price").toString() : "0";
            BigDecimal price = new BigDecimal(priceStr);

            String adminId = productData.getOrDefault("adminId", "").toString().trim();
            int categoryId = productData.get("categoryId") != null ? Integer.parseInt(productData.get("categoryId").toString()) : -1;
            int subcategoryId = productData.get("subcategoryId") != null ? Integer.parseInt(productData.get("subcategoryId").toString()) : -1;
            String storeId = productData.getOrDefault("storeId", "").toString().trim();

            if (categoryId == -1 || subcategoryId == -1) {
                return ResponseEntity.badRequest().body("Error: Missing categoryId or subcategoryId.");
            }

            if (adminId.isEmpty() || storeId.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Admin ID or Store ID is missing.");
            }

            List<String> imageUrlsList = productData.get("imageUrls") instanceof List
                    ? (List<String>) productData.get("imageUrls")
                    : List.of(productData.getOrDefault("imageUrls", "").toString().split(" \\| "));

            Categories category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));

            subCategories subcategory = subCategoryRepository.findById(subcategoryId)
                    .orElseThrow(() -> new RuntimeException("Subcategory not found: " + subcategoryId));

            Product product = new Product();
            product.setName(name);
            product.setDetails(details);
            product.setPrice(price);
            product.setCategories(category);
            product.setSubCategory(subcategory);
            product.setImageUrls(imageUrlsList);
            product.setAdminid(adminId);
            product.setStoreid(storeId);
            product.setActive(true);

            productRepository.save(product);
        }
        return ResponseEntity.ok("CSV products uploaded successfully!");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error processing CSV file: " + e.getMessage());
    }
}

    @PostMapping("/save-category")
    public ResponseEntity<String> saveCategory(@RequestBody Map<String, Object> request) throws Exception {
    	
    	 List<String> categoryNames = (List<String>) request.get("categoryNames");
         String adminId =  request.get("adminId").toString();
         String storeId =  request.get("storeId").toString();

         if ( storeId.equals(null)) {
        	 throw new Exception("customer not found");
         } 
   	  if ( adminId.equals(null)) {
				throw new Exception("customer not found");
			} 
   	  
        for (String name : categoryNames) {
            if (name != null && !name.trim().isEmpty()) {
            	
                Categories category = new Categories();
                category.setName(name.trim());
                category.setAdminid(adminId);
                category.setStoreid(storeId);
                categoryRepository.save(category);
            }
        }
        return ResponseEntity.ok("Categories saved successfully");
    }
    @PostMapping("/save-subcategory")
    public ResponseEntity<String> saveSubCategory(@RequestBody Map<String, Object> request) {
        try {
            int categoryId = Integer.parseInt(request.get("categoryId").toString());  // ✅ Convert String to Integer safely
            List<String> subcategoryNames = (List<String>) request.get("subcategoryNames");
            String adminId =  request.get("adminId").toString();
            String storeId =  request.get("storeId").toString();

            if ( storeId.equals(null)) {
           	 throw new Exception("customer not found");
            } 
      	  if ( adminId.equals(null)) {
				throw new Exception("customer not found");
			} 
            Optional<Categories> categoriesOpt = categoryRepository.findById(categoryId);
            if (categoriesOpt.isPresent()) {
                Categories category = categoriesOpt.get();
                for (String subcategoryName : subcategoryNames) {
                    if (subcategoryName != null && !subcategoryName.trim().isEmpty()) {
                        subCategories subcategory = new subCategories();
                        subcategory.setName(subcategoryName.trim());
                        subcategory.setCategories(category);
                        subcategory.setAdminid(adminId);
                        subcategory.setStoreid(storeId);
                        subCategoryRepository.save(subcategory);
                    }
                }
                return ResponseEntity.ok("Subcategories saved successfully");
            }
            return ResponseEntity.badRequest().body("Category not found");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid request data: " + e.getMessage());
        }
    }


    @PostMapping("/save-product")
    @CrossOrigin(origins = "http://localhost:5173") // ✅ Allow frontend
    public ResponseEntity<String> saveProduct(@RequestBody Map<String, Object> request) {


        try {
        	System.out.println(request.get("adminId"));
        	
            if (!request.containsKey("adminId") || !request.containsKey("categoryId") || !request.containsKey("subcategoryId") || 
                    !request.containsKey("productNames") || !request.containsKey("details") || !request.containsKey("prices") || 
                    !request.containsKey("imageUrls")) {
                    return ResponseEntity.badRequest().body("Missing required fields in request.");
                }
            if (!request.containsKey("categoryId") || !request.containsKey("subcategoryId")) {
                return ResponseEntity.badRequest().body("Missing categoryId or subcategoryId in request");
            }

            int categoryId = Integer.parseInt(request.get("categoryId").toString());
            int subcategoryId = Integer.parseInt(request.get("subcategoryId").toString());
            String adminId =  request.get("adminId").toString();
            String storeId =  request.get("storeId").toString();

            if ( storeId.equals(null)) {
           	 throw new Exception("customer not found");
            } 
        	  if ( adminId.equals(null)) {
  				throw new Exception("customer not found");
  			} 
        	  System.out.println("adminid"+adminId);
            System.out.println("Received categoryId: " + categoryId);
            System.out.println("Received subcategoryId: " + subcategoryId);
            System.out.println("Received request: " + request);
            boolean active = request.containsKey("active") ? Boolean.parseBoolean(request.get("active").toString()) : true;
            
            
//            List<String> productNames = (List<String>) request.getOrDefault("productNames", Collections.emptyList());
            List<Map<String, String>> productNames = (List<Map<String, String>>) request.getOrDefault("productNames", Collections.emptyList());
            
            
            List<String> details = (List<String>) request.getOrDefault("details", Collections.emptyList());
            List<Number> priceNumbers = (List<Number>) request.getOrDefault("prices", Collections.emptyList());
            List<BigDecimal> prices = priceNumbers.stream()
                .map(num -> new BigDecimal(num.toString()))
                .toList();
            List<String> imageUrls = (List<String>) request.getOrDefault("imageUrls", Collections.emptyList());

            if (productNames.isEmpty()) {
                return ResponseEntity.badRequest().body("Product names cannot be empty");
            }

            Optional<Categories> categoriesOptional = categoryRepository.findById(categoryId);
            Optional<subCategories> subCategoriesOptional = subCategoryRepository.findById(subcategoryId);
//            Admin admin = adminRepository.findById(adminId);            
            if (categoriesOptional.isEmpty() || subCategoriesOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid category or subcategory ID");
            }

            Categories category = categoriesOptional.get();
            subCategories subcategory = subCategoriesOptional.get();
         
            
            
            ObjectMapper objectMapper = new ObjectMapper(); 
            
            
            System.out.println(adminId);
            for (int i = 0; i < productNames.size(); i++) {
                Map<String, String> productName = productNames.get(i);
                String jsonProductName = objectMapper.writeValueAsString(productName);

                String detail = details.get(i);
                BigDecimal price = (i < prices.size()) ? prices.get(i) : BigDecimal.ZERO;
                if (productName != null ) {
                    Product product = new Product();
                    product.setName(jsonProductName); 
                    product.setDetails(detail.trim());
                    product.setCategories(category);
                    product.setSubCategory(subcategory);
                    product.setPrice(price);
//                    product.setImageUrls(imageUrls);
                    product.setActive(active);
                    product.setAdminid(adminId);
                    product.setStoreid(storeId);
                    System.out.println(adminId);
                    System.out.println(product.getAdminid());
                    System.out.println(active+"aviuyfcgvhbjnk");
                    if (i < imageUrls.size()) {
                    	List<String> productImageUrls = new ArrayList<>();

                    	Object rawImageUrls = imageUrls.get(i); // Extract the object
                    	if (rawImageUrls instanceof List<?>) {
                    	    for (Object url : (List<?>) rawImageUrls) {
                    	        if (url instanceof String) {
                    	            productImageUrls.add((String) url); // Convert to String and add
                    	        }
                    	    }
                    	}

                    	product.setImageUrls(productImageUrls); // Assign the correct list
                        product.setImageUrls(productImageUrls); // Assign only relevant URLs
                    } else {
                        product.setImageUrls(List.of()); // No images for this product
                    }
                    productRepository.save(product);
                }	
                
            }
            
            return ResponseEntity.ok("Products saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @CrossOrigin(origins = "http://localhost:5173") 
    public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody Map<String, Object> request) {
        try {
     
            Product existingProduct = productRepository.findById(id);
            if (existingProduct == null) {
                return ResponseEntity.badRequest().body("Product not found.");
            }
            String storeId =  request.get("storeId").toString();
            System.out.println(storeId);
            if (!request.containsKey("productNames") || !request.containsKey("details") || !request.containsKey("price") || 
                !request.containsKey("categoryId") || !request.containsKey("subcategoryId") || !request.containsKey("adminId")) {
                return ResponseEntity.badRequest().body("Missing required fields.");
            }
//            String name = (String) request.get("name");
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<Map<String, String>> productNames = (List<Map<String, String>>) request.getOrDefault("productNames", Collections.emptyList());


            // ✅ Extract `productNames` as a single JSON object
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProductName = objectMapper.writeValueAsString(request.get("productNames"));

//            if (productNamesObject == null) {
//                return ResponseEntity.badRequest().body("Product names cannot be null.");
//            }
//            if (productNamesObject instanceof String) {
//                name = objectMapper.readValue((String) productNamesObject, new TypeReference<Map<String, String>>() {});
//            } else if (productNamesObject instanceof Map) {
//                name = (Map<String, String>) productNamesObject;
//            } else {
//                return ResponseEntity.badRequest().body("Invalid productNames format.");
//            }



//            
            
            String details = (String) request.get("details");
            BigDecimal price = new BigDecimal(request.get("price").toString());
            System.out.println("stor iddddd"+storeId);
            if ( storeId.equals(null)) {
           	 throw new Exception("customer not found");
            } 
            int categoryId = Integer.parseInt(request.get("categoryId").toString());
            int subcategoryId = Integer.parseInt(request.get("subcategoryId").toString());
            String adminId =  request.get("adminId").toString();
            
            if (details.isEmpty() || adminId.isEmpty()) {
                return ResponseEntity.badRequest().body("Fields cannot be empty.");
            }
            
      
        
            List<String> imageUrls = (List<String>) request.getOrDefault("imageUrls", Collections.emptyList());

       
            Categories category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
            
            subCategories subcategory = subCategoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new RuntimeException("Subcategory not found with ID: " + subcategoryId));
            boolean active = request.containsKey("active") ? Boolean.parseBoolean(request.get("active").toString()) : existingProduct.isActive();
//            Map<String, String> productName = name.get(i);
            

//            String jsonProductName = objectMapper.writeValueAsString(productNamesObject);
            System.out.println(jsonProductName);
            existingProduct.setName(jsonProductName);
            existingProduct.setDetails(details);
            existingProduct.setPrice(price);
            existingProduct.setCategories(category);
            existingProduct.setSubCategory(subcategory);
            existingProduct.setImageUrls(imageUrls);
            existingProduct.setActive(active); 
            existingProduct.setAdminid(adminId);
            existingProduct.setStoreid(storeId);
            Product savedProduct = productRepository.save(existingProduct);
            return ResponseEntity.ok("saves");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating product: " + e.getMessage());
        }
    }
    
    
    @PutMapping("/toggle-active/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> toggleActiveStatus(@PathVariable int id) {
        try {
            Product product = productRepository.findById(id);
            System.out.println("producy"+product);
               

            product.setActive(!product.isActive());
            productRepository.save(product);

            return ResponseEntity.ok("Product active status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating active status: " + e.getMessage());
        }
    }


}
