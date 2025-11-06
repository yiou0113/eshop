package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 CsrfTokenRequestAttributeHandler csrfTokenRequestHandler = new CsrfTokenRequestAttributeHandler();
	        csrfTokenRequestHandler.setCsrfRequestAttributeName("_csrf");
		http.authorizeHttpRequests(
				auth -> auth
						.requestMatchers("/login", "/register", "/resources/**", "/products/**", "/", "/password/**",
								"/logout", "/users/**", "/uploads/**")
						.permitAll().requestMatchers("/admin/**")
				        .hasRole("ADMIN").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").usernameParameter("email").passwordParameter("password")
						.defaultSuccessUrl("/products", false).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/products").permitAll())
				.csrf(csrf -> csrf
					    .ignoringRequestMatchers("/admin/product/add")
					);
		return http.build();
	}
}
