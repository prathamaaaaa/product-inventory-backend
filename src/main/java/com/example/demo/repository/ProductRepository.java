package com.example.demo.repository;

import com.example.demo.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findById(int id);
    Product deleteById(int id);
	List<Product> findByAdminid(String valueOf);
	List<Product> findByStoreid(String storeId);
	void deleteByStoreid(String storeId);

}