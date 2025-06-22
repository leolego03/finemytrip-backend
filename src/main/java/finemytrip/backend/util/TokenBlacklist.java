package finemytrip.backend.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class TokenBlacklist {
    
    private final ConcurrentMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    
    public void addToBlacklist(String token) {
        blacklistedTokens.put(token, System.currentTimeMillis());
    }
    
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }
    
    public void cleanupExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> 
            currentTime - entry.getValue() > 86400000);
    }
} 