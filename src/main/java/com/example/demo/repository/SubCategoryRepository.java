package com.example.demo.repository;


import com.example.demo.model.subCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<subCategories,Integer> {
}
