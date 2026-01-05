package com.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.entity.Card;
import com.banking.repository.CardRepository;

@Service
public class CardService {

    @Autowired
    CardRepository repo;
    public boolean isCardFrozenByUser(String email){
        Card card = repo.findByEmail(email);
        return card != null && card.isFrozen();
    }

	public void freezeCardByUser(String email, boolean status) {
		Card card = repo.findByEmail(email);
        if (card != null) {
            card.setFrozen(status);
            repo.save(card);
        }
    }
		
}
