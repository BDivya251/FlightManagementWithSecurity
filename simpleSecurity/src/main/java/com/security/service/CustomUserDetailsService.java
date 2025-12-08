//package com.security.service;
//
//import org.springframework.security.core.userdetails.*;
//import org.springframework.stereotype.Service;
//
//import com.security.entity.User;
//import com.security.repository.UserRepository;
//import com.security.security.CustomUserDetails;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository repo;
//
//    public CustomUserDetailsService(UserRepository repo) {
//        this.repo = repo;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username)
//            throws UsernameNotFoundException {
//
//        User user = repo.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//        return new CustomUserDetails(user);
//    }
//}
