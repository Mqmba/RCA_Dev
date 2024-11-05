package be.api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {
    private static final String TOKEN_BLACKLIST_PREFIX = "blacklisted: ";
    private final RedisTemplate<String, String> redisTemplate;

    public void setTokenBlacklist(String token, long expirationInSeconds) {
        redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + token, "true", Duration.ofSeconds(expirationInSeconds));
    }

    public Boolean isTokenBlacklisted(String token) {
        return redisTemplate.opsForValue().get(TOKEN_BLACKLIST_PREFIX + token) != null;
    }
}
