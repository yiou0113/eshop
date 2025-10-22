package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
       
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "password", nullable = false,length = 100)
    private String password;
    @Column(length = 20)
    private String role;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Customer customer;
    
    // Constructors
    public User() {}
    
    public User(String name, String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
      
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() {return role;}
	public void setRole(String role) {this.role = role;}

	public Customer getCustomer() {return customer;}
	public void setCustomer(Customer customer) {this.customer = customer;}

	@Override
    public String toString() {
		return "User{id=" + id + ", email='" + email + "', password='[PROTECTED]'}";
    }
}

