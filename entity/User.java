package com.banking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String gender;
    private String dob;
    private int ficoScore;
    private String theme ="light";
    private String language= "en";
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Account account;


    public User() {}

    public User(String fullName, String email, String password, String phone, String address, String gender, String dob, int ficoScore,String theme, String language) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.ficoScore = ficoScore;
        this.theme = theme;
        this.language = language;
        
    }

    // Getters and Setters
    public Long getId() {
    	return id; 
    	}
    public void setId(Long id) {
    	this.id = id; 
    	}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    
    public Account getAccount() { return account; }
    public void setAccount(Account account) { 
        this.account = account;
    }
    public int getFicoScore() {
        return ficoScore;
    }

    public void setFicoScore(int ficoScore) {
        this.ficoScore = ficoScore;
    }
    public String getTheme() {
    	return theme;
    }
    
    public void setTheme(String theme) {
    	this.theme = theme;
    }
    public String getLanguage() {
    	return language;
    }
    
    public void setLanguage(String language) {
    	this.language = language;
    }
}
