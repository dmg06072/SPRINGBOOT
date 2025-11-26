package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "채팅 메시지")
public class ChatMessage {
    
    @Schema(description = "메시지 타입", example = "TALK")
    public enum MessageType {
        @Schema(description = "채팅방 참여")
        ENTER,
        @Schema(description = "대화 나누기")
        TALK,
        @Schema(description = "채팅방 퇴장")
        QUIT
    }

    @Schema(description = "메시지 타입", example = "TALK", required = true)
    private MessageType type;
    
    @Schema(description = "채팅방 ID", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    private String roomId;
    
    @Schema(description = "메시지 보낸 사람", example = "홍길동", required = true)
    private String sender;
    
    @Schema(description = "메시지 내용", example = "안녕하세요!")
    private String message;
}

