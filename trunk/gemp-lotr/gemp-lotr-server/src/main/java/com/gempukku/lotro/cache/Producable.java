package com.gempukku.lotro.cache;

public interface Producable<T, U> {
    public U produce(T key);
}
