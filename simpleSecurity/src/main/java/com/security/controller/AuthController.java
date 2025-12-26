
package com.security.controller;

import java.security.Key;
import java.util.Map;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.web.bind.annotation.*;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.security.dto.DiscordAuthReuest;
import com.security.dto.GoogleTokenRequest;
import com.security.entity.TokenBlacklist;
import com.security.entity.User;
import com.security.repository.TokenBlacklistRepository;
import com.security.repository.UserRepository;
import com.security.service.PasswordResetService;
import com.security.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestTemplate;

import java.util.Map;


@RestController
public class AuthController {
	 private final String SECRET = "mysecretkeymysecretkeymysecretkey"; // same as auth service
    @Autowired
    private TokenBlacklistRepository blacklistRepo;

    @Autowired
    private UserRepository repo;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Value("${discord.client.id}")
    private String discordClientId;

    @Value("${discord.client.secret}")
    private String discordClientSecret;

    @Value("${discord.redirect.uri}")
    private String discordRedirectUri;

    @Autowired
    private RestTemplate restTemplate;

    
    private final GoogleIdTokenVerifier verifier;
    public AuthController(GoogleIdTokenVerifier verifier) {
        this.verifier = verifier;
    }
    
    @GetMapping("/mail-test")
    public String testMail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("test@example.com");
        msg.setSubject("Test");
        msg.setText("Hello from Spring");

        mailSender.send(msg);
        JavaMailSenderImpl impl = (JavaMailSenderImpl)mailSender;
        System.out.println("SMTP HOST: " + impl.getHost());
        System.out.println("SMTP PORT: " + impl.getPort());
        System.out.println("SMTP USER: " + impl.getUsername());
//        System.out.println("SMTP HOST = "+mailSender.getJavaMailProperties().get("mail.smpt.host"));
        return "sent";
    }

    
    
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        passwordResetService.sendResetLink(email);
        return "Password reset link sent to email";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        passwordResetService.resetPassword(token, newPassword);
        return "Password updated successfully";
    }
    
    
    
    @PostMapping("/google")
    public Map<String, String> googleLogin(@RequestBody GoogleTokenRequest request) throws Exception {

        GoogleIdToken idToken = verifier.verify(request.getToken());

        if (idToken == null) {
            throw new RuntimeException("Invalid Google token");
        }

        String email = idToken.getPayload().getEmail();

        User user = repo.findByEmail(email);
        if (user == null) {
            user = User.builder()
                    .email(email)
                    .username(email)
                    .password(null)
                    .role("ROLE_USER")
                    .build();
            user = repo.save(user);
        }
            
//          return "User logged in: " + user.getEmail() + " role: " + user.getRole();
        String token = jwtUtil.generateToken(user);

        return Map.of(
            "token", token,
            "email", user.getEmail(),
            "role", user.getRole()
        );

    }
    
    @PostMapping("/discord")
    public Map<String, String> discordLogin(@RequestBody DiscordAuthReuest request) {

        // 1️ Exchange code → access token
        String tokenUrl = "https://discord.com/api/oauth2/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", discordClientId);
        body.add("client_secret", discordClientSecret);
        body.add("grant_type", "authorization_code");
        body.add("code", request.getCode());
        body.add("redirect_uri", discordRedirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<>(body, headers);

        Map tokenResponse = restTemplate.postForObject(tokenUrl, entity, Map.class);

        String accessToken = (String) tokenResponse.get("access_token");

        if (accessToken == null) {
            throw new RuntimeException("Discord access token not received");
        }

        // 2️ Get Discord user info
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);

        Map userResponse = restTemplate.exchange(
                "https://discord.com/api/users/@me",
                HttpMethod.GET,
                new HttpEntity<>(userHeaders),
                Map.class
        ).getBody();

        String email = (String) userResponse.get("email");
        String username = (String) userResponse.get("username");

        // 3️ Create / reuse user
        User user = repo.findByEmail(email);
        if (user == null) {
            user = User.builder()
                    .username(email)
                    .email(email)
                    .password(null)
                    .role("ROLE_USER")
                    .build();
            user = repo.save(user);
        }

        
        return Map.of(
        	    "token", jwtUtil.generateToken(user),
        	    "email", user.getEmail(),
        	    "username", user.getUsername()
        	);
    }

    
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

