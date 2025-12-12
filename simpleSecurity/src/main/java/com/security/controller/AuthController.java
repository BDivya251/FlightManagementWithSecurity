
package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> register(@RequestBody User user) {
    	User dbuser=repo.findByUsername(user.getUsername());
    	if(dbuser!=null) {
    		return new ResponseEntity("Already existed",HttpStatus.BAD_REQUEST);
    	}
        user.setPassword(encoder.encode(user.getPassword()));
        // role must be ROLE_USER or ROLE_ADMIN
        repo.save(user);
//        return "Registered successfully";
        return new ResponseEntity("Registered Successfully",HttpStatus.CREATED);
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
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
    	 String token1 = authHeader.substring(7);

 	    //  check BLACKLIST
 	    if (blacklistRepo.existsByToken(token1)) {
 	        return new ResponseEntity("user already logged out",HttpStatus.UNAUTHORIZED); // token is logged out
 	    }
    	if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistRepo.save(new TokenBlacklist(null, token));
            return new ResponseEntity("Logged out successfully",HttpStatus.NO_CONTENT);
        }
    	return new ResponseEntity("user already logged out",HttpStatus.UNAUTHORIZED); // token is logged out
  	   
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

