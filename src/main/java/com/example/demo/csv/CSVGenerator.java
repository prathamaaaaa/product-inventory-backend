package com.example.demo.csv;

import com.example.demo.model.Product;
import java.io.PrintWriter;
import java.util.List;

public class CSVGenerator {

    public static void generateProductCSV(PrintWriter writer, List<Product> products) {
        try {
            // ✅ Include Category ID and Subcategory ID in the CSV Header
            writer.println("Product Name,Details,Price,Image URLs,Category ID,Subcategory ID,Category Name,Subcategory Name");

            for (Product product : products) {
                // ✅ Handle null values safely
                String name = escapeCSV(product.getName());
                String details = escapeCSV(product.getDetails());
                String price = product.getPrice() != null ? product.getPrice().toString() : "0";
                
                // ✅ Convert image URLs to string (handle null cases)
                String imageUrls = product.getImageUrls() != null && !product.getImageUrls().isEmpty()
                        ? "\"" + String.join(" | ", product.getImageUrls()) + "\"" 
                        : "\"\"";

                // ✅ Ensure category ID and subcategory ID are included
                String categoryId = (product.getCategories() != null) ? String.valueOf(product.getCategories().getId()) : "0";
                String subcategoryId = (product.getSubCategory() != null) ? String.valueOf(product.getSubCategory().getId()) : "0";

                // ✅ Include category and subcategory names for reference
                String categoryName = (product.getCategories() != null) ? escapeCSV(product.getCategories().getName()) : "Unknown";
                String subcategoryName = (product.getSubCategory() != null) ? escapeCSV(product.getSubCategory().getName()) : "Unknown";
                System.out.println(categoryId);
                System.out.println(subcategoryId);

                // ✅ Write to CSV
                writer.println(name + "," + details + "," + price + "," + imageUrls + "," + categoryId + "," + subcategoryId + "," + categoryName + "," + subcategoryName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV file: " + e.getMessage());
        }
    }

    // ✅ Escapes special CSV characters (quotes, commas, and newlines)
    private static String escapeCSV(String data) {
        if (data.contains("\"") || data.contains(",") || data.contains("\n")) {
            data = data.replace("\"", "\"\""); // Escape quotes by doubling them
            return "\"" + data + "\""; // Wrap the whole field in quotes
        }
        return data;
    }
}
