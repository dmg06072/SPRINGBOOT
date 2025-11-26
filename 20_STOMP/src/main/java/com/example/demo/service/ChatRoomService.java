package com.example.demo.service;

import com.example.demo.dto.ChatRoom;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatRoomService {

    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    // 1. 채팅방 생성
    public ChatRoom createRoom(String roomName) {
        ChatRoom chatRoom = ChatRoom.create(roomName);
        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    // 채팅방 목록 조회
    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    // 채팅방 단건 조회
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }
}

