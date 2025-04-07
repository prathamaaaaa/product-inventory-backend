package com.example.demo.controller;

import java.util.HashMap;

import java.util.Map;

import java.util.TreeMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CheckoutDetail;
import com.example.demo.model.PaymentDetails;
import com.example.demo.repository.CheckoutRepository;
import com.example.demo.repository.PaymentRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "http://localhost:5173")
public class CheckoutController {
	
	
	 @Autowired
	 private CheckoutRepository checkoutRepository;

	 @Autowired
	 private PaymentRepository paymentRepository;
	 
	   private final String key = "rzp_test_k6Pox5bCFlqv8t";
	    private final String secret = "ZznAUEwNgBzlUpnkF9J29sfs";

	    @PostMapping("/payment-details")
	    public ResponseEntity<?> savePayment(@RequestBody Map<String, String> paymentPayload) {
	        System.out.println(paymentPayload);

	        String orderId = paymentPayload.get("orderid");
	        String paymentId = paymentPayload.get("paymentid");
	        String key = paymentPayload.get("razorpayKey"); // Use correct key name
	        double amount = 0;
	        int userId = 0;

	        try {
	            userId = Integer.parseInt(paymentPayload.get("userid"));
	            if (paymentPayload.containsKey("amount")) {
	                amount = Double.parseDouble(paymentPayload.get("amount"));
	            }
	        } catch (NumberFormatException e) {
	            return ResponseEntity.badRequest().body("Invalid number format in user ID or amount");
	        }

	        PaymentDetails existing = paymentRepository
	                .findTopByUseridAndPaymentidIsNullOrderByIdDesc(userId);

	        if (existing != null && (orderId != null || paymentId != null || amount > 0)) {

	        	if (orderId != null) existing.setOrderid(orderId);
	            if (paymentId != null) existing.setPaymentid(paymentId);
	            if (key != null) existing.setRazopaykey(key);
	            if (paymentPayload.containsKey("amount")) existing.setAmount(amount);
	            existing.setStatus(true);
	            paymentRepository.save(existing);
	            return ResponseEntity.ok("Payment info updated");
	        } else {

	        	PaymentDetails newEntry = new PaymentDetails();
	            newEntry.setUserid(userId);
	            newEntry.setRazopaykey(key);
	            newEntry.setOrderid(orderId);
	            newEntry.setPaymentid(paymentId);
	            newEntry.setAmount(amount);
	            newEntry.setStatus(paymentId != null);
	            paymentRepository.save(newEntry);
	            return ResponseEntity.ok("New payment entry created");
	        }
	    }

	    
	    
	    @PostMapping("/refund")
	    public ResponseEntity<String> refundPayment(@RequestBody Map<String, String> request) {
	        String paymentId = request.get("paymentId");
	        String url = "https://api.razorpay.com/v1/payments/" + paymentId + "/refund";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setBasicAuth(key, secret);
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        Map<String, Object> body = new HashMap<>();

	        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

	        try {
	            RestTemplate restTemplate = new RestTemplate();
	            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
	            return ResponseEntity.ok(response.getBody());
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Refund failed: " + e.getMessage());
	        }
	    }
	    @PostMapping("/create-order")
	    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
	      
	    	System.out.println("ppppp");
	    	System.out.println("ppppp"+data);

	    	RazorpayClient razorpay = new RazorpayClient(key, secret);

	        int amount = Integer.parseInt(data.get("amount").toString()) * 100; 

	        JSONObject orderRequest = new JSONObject();
	        orderRequest.put("amount", amount);
	        orderRequest.put("currency", "INR");
	        orderRequest.put("receipt", "order_rcptid_11");
	        orderRequest.put("payment_capture", true);

	        Order order = razorpay.orders.create(orderRequest);

	        Map<String, Object> response = new HashMap<>();
	        response.put("orderId", order.get("id"));
	        response.put("amount", order.get("amount"));
	        response.put("currency", order.get("currency"));
	        response.put("key", key);

	        return ResponseEntity.ok(response);
	    }
	

	 @PostMapping("/save")
	    public CheckoutDetail saveCheckout(@RequestBody Map<String, Object> checkoutData) {
	        System.out.println("Received Checkout Details: " + checkoutData);
	        System.out.println("Received Checkout Detaisssssssssssssssssssssssssls: " + checkoutData.get("firstname"));
	        
	        String fn = (String) checkoutData.get("firstname");
	        
	        String ln = (String) checkoutData.get("lastname");
	        String a1 = (String) checkoutData.get("address1");
	        String a2 = (String) checkoutData.get("address2");
	        String m = (String) checkoutData.get("mobile");
	        Long zc = Long.parseLong(checkoutData.get("zipcode").toString()); 
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

	        return checkoutRepository.save(checkoutDetail);
	    }
	    
}
