//package com.security.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import com.security.entity.TokenBlacklist;
//import com.security.entity.User;
//import com.security.repository.TokenBlacklistRepository;
//import com.security.repository.UserRepository;
//import com.security.util.JwtUtil;
//
//@RestController
//public class AuthController {
//	@Autowired
//	private TokenBlacklistRepository blacklistRepo;
//
//    private final UserRepository repo;
//    private final PasswordEncoder encoder;
//    private final JwtUtil jwtUtil;
//
//    public AuthController(UserRepository repo, PasswordEncoder encoder, JwtUtil jwtUtil) {
//        this.repo = repo;
//        this.encoder = encoder;
//        this.jwtUtil = jwtUtil;
//    }
//
//    @PostMapping("/register")
//    public String register(@RequestBody User user) {
//    	 if (user.getRole() == null || user.getRole().isBlank()) {
//    	        user.setRole("ROLE_USER"); 
//    	    }
//        user.setPassword(encoder.encode(user.getPassword()));
////        user.setRole("USER");
//        repo.save(user);
//        return "Registered successfully";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody User user) {
//        User dbUser = repo.findByUsername(user.getUsername());
//        if (dbUser != null && encoder.matches(user.getPassword(), dbUser.getPassword())) {
//            return jwtUtil.generateToken(dbUser); // send full user (role included)
//        }
//        return "Invalid username or password";
//    }
//
//    @GetMapping("/hello")
//    public String hello() {
//        return "Hello, token verified!";
//    }
//    @PostMapping("/logout")
//    public String logout(@RequestHeader("Authorization") String authHeader) {
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            blacklistRepo.save(new TokenBlacklist(null, token));
//            return "Logged out successfully";
//        }
//        return "Token missing";
//    }
//    @GetMapping("/admin")
//    public String admin() {
//        return "ADMIN ACCESS";
//    }
//
//    @GetMapping("/user")
//    public String user() {
//        return "USER ACCESS";
//    }
//
//}
package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.security.entity.TokenBlacklist;
import com.security.entity.User;
import com.security.repository.TokenBlacklistRepository;
import com.security.repository.UserRepository;
import com.security.util.JwtUtil;

@RestController
public class AuthController {

    @Autowired
    private TokenBlacklistRepository blacklistRepo;

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        // role must be ROLE_USER or ROLE_ADMIN
        repo.save(user);
        return "Registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User dbUser = repo.findByUsername(user.getUsername());
        if (dbUser != null && encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return jwtUtil.generateToken(dbUser);
        }
        return "Invalid username or password";
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistRepo.save(new TokenBlacklist(null, token));
            return "Logged out successfully";
        }
        return "Token missing";
    }
    
    @GetMapping("/validate-token")
    public boolean validateToken(@RequestHeader("Authorization") String authHeader) {
    	  if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    	        return false;
    	    }

    	    String token = authHeader.substring(7);

    	    //  check BLACKLIST
    	    if (blacklistRepo.existsByToken(token)) {
    	        return false; // token is logged out
    	    }

    	    try {
    	        jwtUtil.extractUsername(token); // if token invalid → throws exception
    	        return true;
    	    } catch (Exception e) {
    	        return false;
    	    } // true = valid, false = blacklisted
    }

}

