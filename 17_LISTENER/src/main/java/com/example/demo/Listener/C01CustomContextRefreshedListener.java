package com.example.demo.Listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class C01CustomContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent>
{
    // 아무거나 달아보시져 ---!!!
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("C01CustomContextRefreshedListener's onApplicationEvent invoke!!!.."+event.getSource());
    }
}
