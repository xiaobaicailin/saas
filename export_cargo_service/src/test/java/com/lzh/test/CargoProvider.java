package com.lzh.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CargoProvider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        ac.start();
        System.in.read();
    }
}
