package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CheckoutDetail;
import com.example.demo.repository.CheckoutRepository;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "http://localhost:5173")
public class CheckoutController {
	
	
	
	 @Autowired
	    private CheckoutRepository checkoutRepository;

	 @PostMapping("/save")
	    public CheckoutDetail saveCheckout(@RequestBody Map<String, Object> checkoutData) {
	        System.out.println("Received Checkout Details: " + checkoutData);
	        System.out.println("Received Checkout Detaisssssssssssssssssssssssssls: " + checkoutData.get("firstname"));
	        
	        String fn = (String) checkoutData.get("firstname");
	        
	        String ln = (String) checkoutData.get("lastname");
	        String a1 = (String) checkoutData.get("address1");
	        String a2 = (String) checkoutData.get("address2");
	        String m = (String) checkoutData.get("mobile");
	        Long zc = Long.parseLong(checkoutData.get("zipcode").toString()); // Convert safely
	        String c = (String) checkoutData.get("city");
	        String s = (String) checkoutData.get("state");
	        int uid = Integer.parseInt(checkoutData.get("userid").toString());
	        
	        CheckoutDetail checkoutDetail = new  CheckoutDetail();
	        checkoutDetail.setFirstname(fn);
	        checkoutDetail.setLastname(ln);
	        checkoutDetail.setAddress1(a1);
	        checkoutDetail.setAddress2(a2);
	        checkoutDetail.setMobile(m);
	        checkoutDetail.setZipcode(zc);
	        checkoutDetail.setCity(c);
	        checkoutDetail.setState(s);
	        checkoutDetail.setUserid(uid);


	        // Save to database
	        return checkoutRepository.save(checkoutDetail);
	    }
	 
	 

	    
}
