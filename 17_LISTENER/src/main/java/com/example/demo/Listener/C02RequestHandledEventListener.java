package com.example.demo.Listener;

import org.springframework.context.ApplicationListener;
import org.springframework.web.context.support.RequestHandledEvent;

public class C02RequestHandledEventListener
implements ApplicationListener<RequestHandledEvent>
{

    @Override
    public void onApplicationEvent(RequestHandledEvent event) {
        System.out.println("C02RequestHandledEventListener's onApplicationEvent.."+event.getSource());
    }
}
