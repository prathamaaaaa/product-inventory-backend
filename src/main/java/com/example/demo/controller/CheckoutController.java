package com.example.demo.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.CheckoutDetail;
import com.example.demo.model.Coupon;
import com.example.demo.model.Orders;
import com.example.demo.model.PaymentDetails;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CheckoutRepository;
import com.example.demo.repository.CouponRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.transaction.Transactional;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
	
	
	 @Autowired
	 private CheckoutRepository checkoutRepository;
	 
	 
	 @Autowired
	 private OrderRepository orderRepository;

	 @Autowired
	 private PaymentRepository paymentRepository;
	 
	 @Autowired
	 private CouponRepository couponRepository;
	 
	 @Autowired
	 private CartRepository cartRepository;

	 
	 
	
	   private final String key = "rzp_test_k6Pox5bCFlqv8t";
	    private final String secret = "ZznAUEwNgBzlUpnkF9J29sfs";

	    
	
	    @GetMapping("/orders/{userId}")
	    public ResponseEntity<?> getOrdersByUserId(@PathVariable int userId) {
	        System.out.println("come");
	        try {
	        	
	            List<Orders> orders = orderRepository.findByUserid(userId);
	            return ResponseEntity.ok(orders);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	        }
	    }

	    
	    
	    @DeleteMapping("/order/{orderid}/{id}")
	    public ResponseEntity<String> deleteOrder(@PathVariable int id , @PathVariable String orderid) {
	        System.out.println("checks dknjdjkdjwbjfsjfs");
	        System.out.println("orderid"+orderid);
	        System.out.println("id"+id);
	        Optional<Orders> optionalOrder = orderRepository.findByOrderidAndProductid(orderid, id);

	        if (optionalOrder.isPresent()) {
	            orderRepository.delete(optionalOrder.get());
	            return ResponseEntity.ok("Order deleted successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
	        }
	    }
	    
	    @PostMapping("/payment-details")
	    @Transactional
	    public ResponseEntity<?> savePayment(@RequestBody Map<String, String> paymentPayload) {
	        System.out.println(paymentPayload);

	        String orderId = paymentPayload.get("orderid");
	        String paymentId = paymentPayload.get("paymentid");
	        String code = paymentPayload.get("couponcode");

	        String key = paymentPayload.get("razorpayKey");
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
	            existing.setCreatedAt(LocalDateTime.now());
	            if (paymentPayload.containsKey("amount")) existing.setAmount(amount);
	            existing.setStatus(true);
	            
	            
	            paymentRepository.save(existing);
	            System.out.println(existing+"eeeeeeeeeeeeeeeeee");
	            
	            
	            String cartItemsJson = paymentPayload.get("cartItems");

	         
	            
//	            
//	            Coupon coupon = couponRepository.findByCode(code);
//	            
//	            coupon.setUsercount(coupon.getUsercount() + 1);
//	            
//	            if (coupon.getUsercount() > 10) {
//					System.out.println("getting");
//					coupon.setActive(false);
//				}
//	            
	            
	            
	            
	            Coupon coupon = couponRepository.findByCode(code);

	            if (coupon != null) {
	                coupon.setUsercount(coupon.getUsercount() + 1);

	                if (coupon.getUsercount() > 10) {
	                    System.out.println("getting");
	                    coupon.setActive(false);
	                }

	                couponRepository.save(coupon); // make sure to save the updated coupon
	            }

	            System.out.println(cartItemsJson);
	            try {
	                ObjectMapper mapper = new ObjectMapper();
	                List<Map<String, Object>> cartItems = mapper.readValue(cartItemsJson, List.class);
	                for (Map<String, Object> item : cartItems) {
	                    int productId = (int) item.get("productid");
	                    int quantity = (int) item.get("quantity");
	                    System.out.println(item.get("price"));
	                    int price = (int) item.get("price");

	                    String productname = item.get("productname").toString();
	    	            ObjectMapper objectMapper = new ObjectMapper();
	    	            Map<String, String> productMap = objectMapper.readValue(productname, Map.class);

	    	            System.out.println(code);
	    	      
	    	            
	    	            System.out.println("English Name: " + productMap.get("en")); // Laptop
	    	            String englishname = productMap.get("en");
	    	            
	    	            
	                    // Optional: If price is passed in cart item
//	                    if (item.containsKey("price")) {
//	                        Object priceObj = item.get("price");
//	                        if (priceObj instanceof Number) {
//	                            price = ((Number) priceObj).doubleValue();
//	                        }
//	                    }

	                    Orders order = new Orders();
	                    order.setUserid(userId);
	                   order.setProductname(englishname);
	                    order.setProductid(productId);
	                    order.setQuantity(quantity);
	                    order.setOrderdate(LocalDate.now());
	                    order.setOrderid(orderId);
	                    order.setPrice(price); 
	                    orderRepository.save(order);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to parse cartItems");
	            }

	            
	            cartRepository.deleteByUserid(userId);   
	           
	            return ResponseEntity.ok("Payment info updated");
	            
	            
	            
	        } else {

	        	PaymentDetails newEntry = new PaymentDetails();
	            newEntry.setUserid(userId);
	            newEntry.setRazopaykey(key);
	            newEntry.setOrderid(orderId);
	            newEntry.setPaymentid(paymentId);
	            newEntry.setAmount(amount);
	            newEntry.setCreatedAt(LocalDateTime.now());
	            newEntry.setStatus(paymentId != null);
	            paymentRepository.save(newEntry);
	            System.out.println(newEntry+"newwwwwwwwwwwwww");
	            


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
