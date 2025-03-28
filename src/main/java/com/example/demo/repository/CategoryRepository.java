package com.example.demo.repository;

import com.example.demo.model.Categories;
import com.example.demo.model.Product;
import com.example.demo.model.subCategories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categories, Integer> {
//	List<Categories> findByAdminid(int adminId);
	List<Categories> findByAdminid(String valueOf);

    Categories deleteById(int id);
	List<Categories> findByStoreid(String storeId);

}
