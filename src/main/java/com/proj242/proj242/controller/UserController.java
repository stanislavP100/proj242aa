package com.proj242.proj242.controller;


import com.proj242.proj242.entity.Role;
import com.proj242.proj242.entity.User;
import com.proj242.proj242.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String getRegistrationPage()
    {
        return "registration";
    }

    @PostMapping("/registration")
    public String postRegistrationPage(User user,
                                       Model model)
    {
        if(userRepository.findByUsername(user.getUsername())!=null)
        {
            model.addAttribute("message", "user with this username exists");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        model.addAttribute("message", "new user added");

        return "/registration";
    }
}
