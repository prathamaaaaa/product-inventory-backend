package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.SubCategoryDTO;
import com.example.demo.model.Coupon;
import com.example.demo.repository.CouponRepository;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
	
	@Autowired
	private CouponRepository couponRepository;
	
	    @GetMapping("/all")
	    public List<Coupon> getAllCoupons() {
	        return couponRepository.findAll();
	    }
	 
	    @PostMapping("/add")
	    public Coupon addCoupon(@RequestBody Map<String, String> params) {
	    	Coupon coupon = new Coupon();
	    	String ss = params.get("code");
	    	System.out.println(ss);
	    	int discount =Integer.parseInt( params.get("discount"));
	    	System.out.println(discount);

	    	int minAmount =Integer.parseInt( params.get("minAmount"));
	    	System.out.println(minAmount);

	    	boolean active = Boolean.parseBoolean(params.get("isActive"));
	    	System.out.println(active);
	    	
	    	coupon.setCode(ss);
	    	coupon.setDiscount(discount);
	    	coupon.setMinAmount(minAmount);
	    	coupon.setUsercount(0);
	    	coupon.setActive(active);
	        return couponRepository.save(coupon);
	    }

	    @PutMapping("/{id}/status")
	    public ResponseEntity<Coupon> updateCouponStatus(@PathVariable int id, @RequestBody Coupon updatedCoupon) {
	        Optional<Coupon> optionalCoupon = couponRepository.findById(id);
	        if (optionalCoupon.isPresent()) {
	            Coupon coupon = optionalCoupon.get();
	            coupon.setActive(updatedCoupon.isActive());
	            couponRepository.save(coupon);
	            return ResponseEntity.ok(coupon);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    @DeleteMapping("/{id}")
	    public ResponseEntity<String> deleteCoupon(@PathVariable int id){
	    	couponRepository.deleteById(id);
	    	return ResponseEntity.ok("deleted");
	    }

}
