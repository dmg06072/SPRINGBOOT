package com.example.demo.Listener;

import org.springframework.context.ApplicationListener;

public class MemoAddEventListener
        implements ApplicationListener<MemoAddEvent>
{

    @Override
    public void onApplicationEvent(MemoAddEvent event) {
        System.out.println("[MEMO ADD EVENT] " + event);
    }
}
