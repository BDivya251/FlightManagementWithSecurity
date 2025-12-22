
package com.security.controller;

import java.security.Key;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class AuthController {
	 private final String SECRET = "mysecretkeymysecretkeymysecretkey"; // same as auth service
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
    	System.out.println("regsitered"+user.getUsername());
    	System.out.println(user.getPassword());
//    	System.out.println()
    	User dbuser=repo.findByUsername(user.getUsername());
    	User dbemail = repo.findByEmail(user.getEmail());
    	if(dbuser!=null || dbemail!=null) {
    		System.out.println("Already existed");
    		return new ResponseEntity("Already existed",HttpStatus.BAD_REQUEST);
    	}
    	if(user.getRole()==null) {
    		user.setRole("USER");
    	}
    	System.out.println("passwordEncoding");
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
    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordReq res) {
    		User dbuser = repo.findByUsername(res.username);
    		 if (dbuser == null) {
    		        return "User not found";
    		    }
    		 if (!encoder.matches(res.oldPassword, dbuser.getPassword())) {
    		        return "Old password is incorrect";
    		    }
    		if(encoder.matches(res.newPassword, dbuser.getPassword())) {
    			return "Give a new Password";
    		}
    		if(dbuser!=null && encoder.matches(res.oldPassword,dbuser.getPassword())) {
    			dbuser.setPassword(encoder.encode(res.newPassword));
    			repo.save(dbuser);
    			System.out.println(res.newPassword);
    			return "Successfully changed";
    		}
    		else {
    			return "Error in changing the password";
    		}
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
    private Key getSigningKey() {
        byte[] keyBytes = SECRET.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7); // remove Bearer

        Claims claims = extractAllClaims(token);

        String username = claims.getSubject();
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        return ResponseEntity.ok(
            Map.of(
                "username", username,
                "email", email,
                "role", role
            )
        );
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

