package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	Optional<Cart> findByProductid(int productid);

		Cart findByUseridAndProductid(int userId, int productid);

	void deleteByUserid(int userId);

	
}
