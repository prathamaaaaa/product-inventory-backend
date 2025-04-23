package com.example.demo.controller;

import com.example.demo.model.Admin;
import com.example.demo.model.Product;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.ProductRepository;

import org.springframework.http.MediaType;
import com.example.demo.service.AdminService;
import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	   @Autowired
	    private AdminService adminService;

	   @Autowired
	   private AdminRepository adminRepository;
	   
	   @Autowired
	    private ProductRepository productRepository;

	   private String escapeCSV(String data) {
		    if (data == null) return "";
		    if (data.contains("\"") || data.contains(",") || data.contains("\n")) {
		        data = data.replace("\"", "\"\""); 
		        return "\"" + data + "\"";
		    }
		    return data;
		}

	   @DeleteMapping("/confirm-delete/{adminId}")
	   public ResponseEntity<String> deleteAdmin(@PathVariable int adminId) {
	       try {
	           System.out.println(" Request received to delete admin and associated products: " + adminId);

	           List<Product> products = productRepository.findByAdminid(String.valueOf(adminId));
	           if (!products.isEmpty()) {
	               productRepository.deleteAll(products);
	               System.out.println("Deleted all products of adminId: " + adminId);
	           }

	           adminRepository.deleteById(adminId);
	           System.out.println("Admin deleted successfully: " + adminId);

	           return ResponseEntity.ok("Admin and all associated products deleted successfully!");
	       } catch (Exception e) {
	           System.err.println(" Error deleting admin: " + e.getMessage());
	           e.printStackTrace();
	           return ResponseEntity.status(500).body("Error deleting admin: " + e.getMessage());
	       }
	   }

	   
	//   @GetMapping("/download-csv/{adminId}")
//	    public ResponseEntity<?> downloadAdminProductsCSV(@PathVariable int adminId) {
//	        try {
//	            System.out.println(" Request received to download CSV for adminId: " + adminId);
//
//	            List<Product> products = productRepository.findByAdminid(String.valueOf(adminId));
//
//	            if (products.isEmpty()) {
//	                System.out.println("No products found for adminId: " + adminId);
//	                return ResponseEntity.badRequest().body("No products found for this admin.");
//	            }
//
//	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//	            PrintWriter writer = new PrintWriter(outputStream);
//
//	            writer.println("Product Name,Details,Price,Image URLs,Category ID,Subcategory ID,Category Name,Subcategory Name");
//
//	            for (Product product : products) {
//	                String name = escapeCSV(product.getName());
//	                String details = escapeCSV(product.getDetails());
//	                String price = (product.getPrice() != null) ? product.getPrice().toString() : "0";
//
//	                String imageUrls = (product.getImageUrls() != null && !product.getImageUrls().isEmpty())
//	                        ? "\"" + String.join(" | ", product.getImageUrls()) + "\""
//	                        : "\"\"";
//
//	                String categoryId = (product.getCategories() != null) ? String.valueOf(product.getCategories().getId()) : "0";
//	                String subcategoryId = (product.getSubCategory() != null) ? String.valueOf(product.getSubCategory().getId()) : "0";
////	                String storeId = (product.getStoreid() != null) ? String.valueOf(product.getStoreid().g()) : "0";
//
//	                String categoryName = (product.getCategories() != null) ? escapeCSV(product.getCategories().getName()) : "Unknown";
//	                String subcategoryName = (product.getSubCategory() != null) ? escapeCSV(product.getSubCategory().getName()) : "Unknown";
//
//	                writer.println(name + "," + details + "," + price + "," + imageUrls + "," + categoryId + "," + subcategoryId + "," + categoryName + "," + subcategoryName);
//	            }
//
//	            writer.flush();
//	            writer.close();
//
//	            byte[] csvBytes = outputStream.toByteArray();
//	            String filename = "admin_products_" + adminId + ".csv";
//
//	            System.out.println("CSV file generated successfully for adminId: " + adminId);
//
//	            return ResponseEntity.ok()
//	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
//	                    .contentType(MediaType.parseMediaType("text/csv"))
//	                    .body(csvBytes);
//
//	        } catch (Exception e) {
//	            System.err.println(" Error generating CSV file for adminId " + adminId + ": " + e.getMessage());
//	            e.printStackTrace();
//	            return ResponseEntity.status(500).body("Error generating CSV file: " + e.getMessage());
//	        }
//	    }
  
	   @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	   public ResponseEntity<String> register(@RequestBody Admin admin) {

	       System.out.println("Received admin object: " + admin);
	       try {
	           adminService.registerAdmin(admin);
	           return ResponseEntity.ok("Admin registered successfully!");
	       } catch (RuntimeException e) {
	           System.err.println("Registration error: " + e.getMessage()); // Add this line
	           return ResponseEntity.badRequest().body(e.getMessage());
	       }
	   }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) { 
        Map<String, Object> response = adminService.loginAdmin(admin.getEmail(), admin.getPassword());

        if (response == null) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Invalid credentials");
        }
        return ResponseEntity.ok(response);
    }
   
    @DeleteMapping("/list/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        adminService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
