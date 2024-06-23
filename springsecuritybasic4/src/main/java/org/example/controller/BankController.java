package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BankController {
    @GetMapping("/")
    public String welcome() {
        return "Welcome to the basic spring security tutorial";
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, String>> userDetails() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Keshav Kumar");
        map.put("roll", "10202");
        map.put("phone", "8909980987");
        map.put("address", "Bhagwanpur");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/statement")
    public String bankStatement() {
        return "Your account balance is \u20B9101 Crore.";
    }

    @GetMapping("/contactus")
    public String contactUs() {
        return "Support is available only in night.";
    }
}
