package com.example.demo.controller;

import com.example.demo.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    // 메시지를 브로커를 통해 특정 사용자/채팅방에 전달하는 역할
    private final SimpMessageSendingOperations messagingTemplate;

    // '/pub/chat/message'로 들어오는 STOMP 메시지 처리
    // MessageMapping의 경로는 Application Destination Prefix ('/pub') 뒤에 붙음
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        // 메시지 타입에 따른 로직 처리 (1. 채팅방 생성, 2. 채팅방 참여, 4. 채팅 종료)
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            // 2. 채팅방 참여 (ENTER): 입장 메시지 생성
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        } else if (ChatMessage.MessageType.QUIT.equals(message.getType())) {
            // 4. 채팅 종료 (QUIT): 퇴장 메시지 생성
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
        }
        
        // 3. 대화 나누기 (TALK): 메시지 전송
        // '/sub/chat/room/{roomId}'를 구독하는 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}

