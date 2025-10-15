package com.example.demo.model;
import java.math.BigDecimal;

import javax.persistence.*;
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private int quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    public void updateSubtotal() {
    this.subtotal = this.price.multiply(BigDecimal.valueOf(this.quantity));
    }

    // getter/setter
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public Order getOrder() {return order;}
	public void setOrder(Order order) {this.order = order;}

	public Product getProduct() {return product;}
	public void setProduct(Product product) {this.product = product;}

	public int getQuantity() {return quantity;}
	public void setQuantity(int quantity) {this.quantity = quantity;}

	public BigDecimal getPrice() {return price;}
	public void setPrice(BigDecimal price) {this.price = price;}

	public BigDecimal getSubtotal() {return subtotal;}
	public void setSubtotal(BigDecimal subtotal) {this.subtotal = subtotal;}   
}
