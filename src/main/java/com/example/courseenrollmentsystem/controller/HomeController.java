package com.example.courseenrollmentsystem.controller;

import com.example.courseenrollmentsystem.dataModel.UserAccount;
import com.example.courseenrollmentsystem.dto.UserAccountDTO;
import com.example.courseenrollmentsystem.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserAccountService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Thymeleaf login.html
    }

    @GetMapping("/logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "login?logout";
    }

    @GetMapping({"/", "/home"})
    public String homePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            UserAccountDTO user = userService.findByUsername(username).orElse(null);

            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("username", user.getUsername());

                // Role-based message
                switch (user.getRole()) {
                    case ADMIN -> model.addAttribute("dashboardMessage", "Admin Dashboard");
                    case SUBADMIN -> model.addAttribute("dashboardMessage", "Subadmin Dashboard");
                }
            } else {
                model.addAttribute("username", "Unknown");
                model.addAttribute("dashboardMessage", "Welcome!");
            }
        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("dashboardMessage", "Please login");
        }

        return "home"; // home.html
    }
}