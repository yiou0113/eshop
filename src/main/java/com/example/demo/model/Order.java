package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();
    
    // getter/setter
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public User getUser() {return user;}
	public void setUser(User user) {this.user = user;}

	public List<OrderItem> getItems() {return items;}
	public void setItems(List<OrderItem> items) {this.items = items;}

	public BigDecimal getTotalAmount() {return totalAmount;}
	public void setTotalAmount(BigDecimal totalAmount) {this.totalAmount = totalAmount;}

	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}

	public LocalDateTime getCreatedAt() {return createdAt;}
	public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

}

