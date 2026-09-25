package com.itheima.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Calendar;
import java.util.Date;

public class TokenUtils {

    public static String generateToken(int userId, String password) {
        return JWT.create()
                .withAudience(String.valueOf(userId))
                .withExpiresAt(offsetHour(new Date(), 2))
                .sign(Algorithm.HMAC256(password));
    }

    public static Date offsetHour(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }
}
