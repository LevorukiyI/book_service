package com.modsensoftware.book_service.utils;

import jakarta.servlet.http.HttpServletRequest;

public class HttpRequestUtils {

    public static String extractSecretKey(HttpServletRequest request){
        return request.getHeader("x-api-key");
    }

    public static String extractAccessToken(HttpServletRequest request){
        final String authorizationHeader = request.getHeader("Authorization");
        return extractAccessToken(authorizationHeader);
    }

    public static String extractAccessToken(String authorizationHeader){
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7);
    }
}
