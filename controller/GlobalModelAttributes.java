package com.banking.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.banking.entity.User;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user != null) {
            model.addAttribute("theme", user.getTheme());
            model.addAttribute("language", user.getLanguage());
        } else {
            model.addAttribute("language", "en");
        }
    }
}
