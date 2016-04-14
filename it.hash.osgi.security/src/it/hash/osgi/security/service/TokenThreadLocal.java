package it.hash.osgi.security.service;

public class TokenThreadLocal {
    public static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<String>();

    public static void set(String token) {
        tokenThreadLocal.set(token);
    }

    public static void unset() {
        tokenThreadLocal.remove();
    }

    public static String get() {
        return tokenThreadLocal.get();
    }
}