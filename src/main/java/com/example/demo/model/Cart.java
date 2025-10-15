package com.example.demo.model;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public User getUser() {return user;}
	public void setUser(User user) {this.user = user;}

	public List<CartItem> getItems() {return items;}
	public void setItems(List<CartItem> items) {this.items = items;}
    
}

