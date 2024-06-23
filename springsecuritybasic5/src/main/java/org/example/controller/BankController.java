package org.example.controller;

import org.example.dao.User;
import org.example.repositories.SpringSEcurityExampleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BankController {
    private final SpringSEcurityExampleRepo repo;

    @Autowired
    public BankController(SpringSEcurityExampleRepo repo) {
        this.repo = repo;
    }
//    If you are using post request while using spring security then you
//    might get 403 error while doing post request. So if you are sure that
//    your application is only to be used by any other application or non-browser
//    application then you must disable csrf and the error will be resolved
    @PostMapping("/adduser")
    public User addUser(@RequestBody User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        User response =repo.save(user);
        return response;
    }

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
