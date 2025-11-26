package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "채팅방 정보")
public class ChatRoom {
    
    @Schema(description = "채팅방 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    private String roomId;
    
    @Schema(description = "채팅방 이름", example = "우리들의 채팅방")
    private String roomName;

    // 채팅방 생성 팩토리 메서드
    public static ChatRoom create(String roomName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;
        return chatRoom;
    }
}

