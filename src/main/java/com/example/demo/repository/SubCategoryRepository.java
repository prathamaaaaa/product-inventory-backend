package com.example.demo.repository;


import com.example.demo.model.Product;
import com.example.demo.model.subCategories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<subCategories,Integer> {
	List<subCategories> findByAdminid(String valueOf);
    subCategories deleteById(int id);

//	List<subCategories> findSubCategoriesByAdminId(int adminId);
	List<subCategories> findByStoreid(String storeId);

}
