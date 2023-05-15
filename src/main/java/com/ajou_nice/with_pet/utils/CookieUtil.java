package com.ajou_nice.with_pet.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public void addCookie(HttpServletResponse response, String name, String value, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path(path)
                .secure(true)
                .sameSite("None")
                .httpOnly(false)
                .maxAge(Math.toIntExact(1 * 24 * 60 * 60))
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }

    public String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] list = request.getCookies();
        if (list == null) {
            return null;
        }
        String value = null;
        for (Cookie cookie : list) {
            if (cookie.getName().equals(key)) {
                value = cookie.getValue();
                return value;
            }
        }
        return null;
    }

    public void initCookie(HttpServletResponse response, String name, String value, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path(path)
                .secure(true)
                .sameSite("None")
                .httpOnly(false)
                .maxAge(Math.toIntExact(0))
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }
}
