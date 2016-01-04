package com.gempukku.mtg.provider;

public interface Retryable<T, U extends Exception> {
    T execute() throws U;

    boolean isRetryable(Exception exception);
}