package com.example.demo.dto;

import com.example.demo.model.Cart;
import com.example.demo.model.Categories;

public class CartDTO {
    private int id;
    private int productid;
	private String productname;
	private int quantity;	
	
    public CartDTO(Cart cart) {
      this.id=cart.getId();
      this.productid = cart.getProductid();
      this.productname = cart.getProductname();
      this.quantity = cart.getQuantity();
        
    }
    
    public int getId() { return id; }

	public int getProductid() {
		return productid;
	}

	public String getProductname() {
		return productname;
	}

	public int getQuantity() {
		return quantity;
	}


}
