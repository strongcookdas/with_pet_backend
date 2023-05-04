package com.ajou_nice.with_pet.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public void saveCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        response.addCookie(cookie);
    }

    public void savePathCookie(HttpServletResponse response, String key, String value,
            String path) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        response.addCookie(cookie);
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


}
