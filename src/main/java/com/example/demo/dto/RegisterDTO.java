package com.example.demo.dto;

import javax.validation.constraints.*;

public class RegisterDTO {

    @NotBlank(message = "姓名不可為空")
    @Size(max = 150, message = "姓名過長")
    private String name;

    @NotBlank(message = "Email 不可為空")
    @Email(message = "Email 格式不正確")
    private String email;

    @NotBlank(message = "密碼不可為空")
    private String password;
    
    @NotBlank(message = "電話不可為空")
    @Size(max = 20, message = "電話過長")
    private String phone;

    @NotBlank(message = "地址不可為空")
    @Size(max = 255, message = "地址過長")
    private String address;

    // Getter & Setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


    
    
    
}
