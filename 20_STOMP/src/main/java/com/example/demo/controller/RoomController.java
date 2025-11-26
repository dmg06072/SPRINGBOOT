package com.example.demo.controller;

import com.example.demo.dto.ChatRoom;
import com.example.demo.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class RoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 목록 페이지
    @GetMapping("/room")
    public String rooms(Model model) {
        List<ChatRoom> rooms = chatRoomService.findAllRoom();
        model.addAttribute("rooms", rooms);
        return "chat/room";
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam("roomName") String roomName) {
        return chatRoomService.createRoom(roomName);
    }

    // 채팅방 입장
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable("roomId") String roomId) {
        ChatRoom room = chatRoomService.findRoomById(roomId);
        if (room == null) {
            return "redirect:/chat/room";
        }
        model.addAttribute("room", room);
        return "chat/roomdetail";
    }
}

