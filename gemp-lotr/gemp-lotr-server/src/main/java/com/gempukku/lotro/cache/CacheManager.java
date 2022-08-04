package com.gempukku.lotro.cache;

import java.util.HashSet;
import java.util.Set;

public class CacheManager {
    private final Set<Cached> _caches = new HashSet<>();

    public void addCache(Cached cached) {
        _caches.add(cached);
    }

    public void clearCaches() {
        for (Cached cache : _caches)
            cache.clearCache();
    }

    public int getTotalCount() {
        int total = 0;
        for (Cached cache : _caches)
            total+=cache.getItemCount();
        return total;
    }
}
