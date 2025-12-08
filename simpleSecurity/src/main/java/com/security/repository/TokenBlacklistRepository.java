package com.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.entity.TokenBlacklist;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByToken(String token);
}
