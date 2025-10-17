package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // getters & setters
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public String getPhone() {return phone;}
	public void setPhone(String phone) {this.phone = phone;}

	public String getAddress() {return address;}
	public void setAddress(String address) {this.address = address;}

	public User getUser() {return user;}
	public void setUser(User user) {this.user = user;}    
}

