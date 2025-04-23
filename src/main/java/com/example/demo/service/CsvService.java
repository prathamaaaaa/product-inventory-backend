package com.example.demo.service;
import com.example.demo.model.Product;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import java.io.StringWriter;
import java.util.List;
import com.opencsv.CSVWriter;

@Service
public class CsvService {

    public byte[] generateCSV(List<Product> products) {
        try (StringWriter writer = new StringWriter();
             CSVWriter csvWriter = new CSVWriter(writer)) {

            // ✅ Write CSV Header
            String[] header = {"Product ID", "Product Name", "Details","Category ID","Subcategory ID", "Price", "Category", "Subcategory", "Image URLs", "Active", "Admin ID", "Store ID"};
            csvWriter.writeNext(header);

            for (Product product : products) {
                csvWriter.writeNext(new String[]{
                		String.valueOf(product.getId()),
                		product.getName(),
                		product.getDetails(),

                		product.getCategories() != null ? String.valueOf(product.getCategories().getId()) : "N/A",

                		product.getSubCategories() != null ? String.valueOf(product.getSubCategories().getId()) : "N/A",

                		String.valueOf(product.getPrice()),

                		// ✅ Get Category Name
                		product.getCategories() != null ? product.getCategories().getName() : "N/A",

                		// ✅ Get Subcategory Name
                		product.getSubCategories() != null ? product.getSubCategories().getName() : "N/A",

                		// ✅ Join multiple image URLs with " | "
                		String.join(" | ", product.getImageUrls()), 

                		String.valueOf(product.isActive()),
                		String.valueOf(product.getAdminid()),
                		String.valueOf(product.getStoreid())

                });
            }

            return writer.toString().getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV file", e);
        }
    }
}
