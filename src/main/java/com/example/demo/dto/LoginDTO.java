package com.example.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginDTO {
	@NotBlank(message="Email 未填寫")
    @Email(message="Email 格式錯誤")
    private String email;

    @NotBlank(message="密碼未填寫")
    private String password;
    
    // getter / setter
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


}
