package com.banking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long cardId;
    private String cardNumber;
    private boolean frozen;
    private String email;
    private boolean intlEnabled;
    
    public boolean isFrozen() {   // getter
        return frozen;
    }

    public void setFrozen(boolean frozen) {  // setter
        this.frozen = frozen;
    }

    public boolean isIntlEnabled() {
        return intlEnabled;
    }

    public void setIntlEnabled(boolean intlEnabled) {
        this.intlEnabled = intlEnabled;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }

}
