package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Store;

public interface StoreRepository  extends JpaRepository<Store, Integer>{

	List<Store> findByAdminid(String adminId);
	
	
}
