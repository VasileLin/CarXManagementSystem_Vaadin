package com.vmoon.carx.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class Generators {

    public static String transactionNoGenerator(){
        String data = LocalDate.now().toString().replace("-", "");
        Random random = new Random();
        return data + random.nextLong(10000, 99999);
    }
}
