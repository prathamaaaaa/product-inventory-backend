package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Orders;
import com.razorpay.Order;

public interface OrderRepository extends JpaRepository<Orders, Integer>{

	List<Orders> findByUserid(int userid); // ✅ correct

	void deleteByProductid(int id);

	Optional<Orders> findByOrderidAndProductid(String orderid, int id);


}
