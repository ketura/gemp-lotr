package com.gempukku.mtg.provider;

public class RetryUtil {
    public static <T, U extends Exception> T executeInRetry(Retryable<T, U> task, int retries) throws U {
        int retryCount = 0;
        while (true) {
            try {
                return task.execute();
            } catch (Exception exp) {
                if (task.isRetryable(exp)) {
                    retryCount++;
                    if (retryCount == retries)
                        throw (U) exp;
                } else if (exp instanceof RuntimeException) {
                    throw (RuntimeException) exp;
                } else {
                    throw new RuntimeException(exp);
                }
            }
        }
    }
}
