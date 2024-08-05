package org.example.controller;

import org.example.configuration.util.JwtUtil;
import org.example.dao.AuthRequest;
import org.example.dao.AuthResponse;
import org.example.dao.User;
import org.example.repositories.SpringSEcurityExampleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BankController {
    private final SpringSEcurityExampleRepo repo;

    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    @Autowired
    public BankController(SpringSEcurityExampleRepo repo,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.repo = repo;
        this.authenticationManager =authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            String jwt = jwtUtil.generateToken(authentication.getName());
            System.out.println(jwt);
            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
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
        User response = repo.save(user);
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

    @GetMapping("/cmd")
    public String runCMD() throws IOException {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        Process process = null;
        if (isWindows) {
            try {
                process = Runtime.getRuntime()
                        .exec(String.format("ipconfig"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        BufferedReader reader =
                new BufferedReader (new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine ()) != null) {
            System.out.println ("Stdout: " + line);
        }
//        System.out.println(process.getOutputStream());
        return "";
    }

}
