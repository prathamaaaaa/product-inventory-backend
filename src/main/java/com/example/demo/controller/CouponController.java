package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.SubCategoryDTO;
import com.example.demo.model.Coupon;
import com.example.demo.repository.CouponRepository;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/coupons")
public class CouponController {
	
	@Autowired
	private CouponRepository couponRepository;
	
	    @GetMapping("/all")
	    public List<Coupon> getAllCoupons() {
	        return couponRepository.findByIsActiveTrue();
	    }
	 
}
