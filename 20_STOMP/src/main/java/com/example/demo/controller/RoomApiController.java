package com.example.demo.controller;

import com.example.demo.dto.ChatRoom;
import com.example.demo.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
@Tag(name = "채팅방 API", description = "채팅방 관리 API")
public class RoomApiController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    @Operation(summary = "채팅방 목록 조회", description = "생성된 모든 채팅방 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", 
                 content = @Content(schema = @Schema(implementation = ChatRoom.class)))
    public ResponseEntity<List<ChatRoom>> getAllRooms() {
        List<ChatRoom> rooms = chatRoomService.findAllRoom();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성합니다. 고유한 roomId가 자동으로 생성됩니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "채팅방 생성 성공",
                     content = @Content(schema = @Schema(implementation = ChatRoom.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<ChatRoom> createRoom(
            @Parameter(description = "채팅방 이름", required = true, example = "우리들의 채팅방")
            @RequestParam("roomName") String roomName) {
        ChatRoom room = chatRoomService.createRoom(roomName);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "채팅방 조회", description = "roomId로 특정 채팅방 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
                     content = @Content(schema = @Schema(implementation = ChatRoom.class))),
        @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음")
    })
    public ResponseEntity<ChatRoom> getRoom(
            @Parameter(description = "채팅방 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable("roomId") String roomId) {
        ChatRoom room = chatRoomService.findRoomById(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }
}

