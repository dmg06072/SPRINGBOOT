package com.example.demo.Listener;

import com.example.demo.Model.Dto.MemoDto;
import org.springframework.context.ApplicationEvent;

public class MemoAddEvent  extends ApplicationEvent {
    private MemoDto dto;
    public MemoAddEvent(Object source,MemoDto dto)
    {
        super(source);
        this.dto = dto;
    }

    @Override
    public String toString() {
        return "MemoAddEvent{" +
                "dto=" + dto +
                '}';
    }
}
