package com.financialapplication.tijori.Service;

import com.financialapplication.tijori.Config.CacheConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenStore {

    private final CacheManager cacheManager;

    public void store(String mobileNo, String tokenId) {
        cache().put(mobileNo, tokenId);
    }

    public boolean matches(String mobileNo, String tokenId) {
        String storedTokenId = cache().get(mobileNo, String.class);
        return storedTokenId != null && storedTokenId.equals(tokenId);
    }

    public void revoke(String mobileNo) {
        cache().evict(mobileNo);
    }

    private Cache cache() {
        Cache cache = cacheManager.getCache(CacheConfig.REFRESH_TOKEN_CACHE);
        if (cache == null) {
            throw new IllegalStateException("Refresh token cache is not configured");
        }
        return cache;
    }
}