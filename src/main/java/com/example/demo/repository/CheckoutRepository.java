package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CheckoutDetail;

public interface CheckoutRepository extends JpaRepository<CheckoutDetail, Integer> {

}
