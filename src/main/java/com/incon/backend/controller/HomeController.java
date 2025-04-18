package com.incon.backend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "Welcome to Incon Marketplace API";
    }

    // You could also add a simple health check endpoint
    @GetMapping("/api/health")
    @ResponseBody
    public String healthCheck() {
        return "{\"status\": \"UP\"}";
    }
}
