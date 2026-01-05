package com.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.banking.security.CustomUserDetails;

@Configuration
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        
	        .authorizeHttpRequests(auth -> auth
	        		
	        		.requestMatchers("/", "/register", "/login", "/send-reset-otp", "/verify-reset-otp", "/reset-password",
	                        "/send-register-otp", "/verify-register-otp",
	                        "/css/**", "/js/**","/Bankinglogo.png", "/images/**", "/fonts/**", "/assets/**").permitAll()

	                
	                .anyRequest().authenticated()
	        )

	        .formLogin(form -> form
	        		.loginPage("/login") 
	        		.loginProcessingUrl("/login")// **very important**
	        		.usernameParameter("email")
	        		.passwordParameter("password")
	                .successHandler(authenticationSuccessHandler()) // use only this
	        		.failureUrl("/login?error")  // <-- add this
	        		.permitAll()
	        )
	        .logout(logout -> logout.permitAll());

	    return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	
	 // Store logged-in user in session
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            request.getSession().setAttribute("loggedInUser", userDetails.getUser());
            if(!response.isCommitted()) {
                response.sendRedirect("/");
            }
        };
    }
}












