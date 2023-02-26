package com.hexanome.fourteen;

public class TokenRefreshFailedException extends Exception {
    public TokenRefreshFailedException(){ super("Tokens could not be refreshed"); }
    public TokenRefreshFailedException(String errorMessage) {
        super("Tokens could not be refreshed:\n" + errorMessage);
    }
}
