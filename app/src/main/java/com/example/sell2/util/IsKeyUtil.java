package com.example.sell2.util;

import java.util.Random;

public class IsKeyUtil {
    public static synchronized String key()
    {
        Random random=new Random();
        Integer a=random.nextInt(9000000)+100000;
        return System.currentTimeMillis()+String.valueOf(a);
    }
}
