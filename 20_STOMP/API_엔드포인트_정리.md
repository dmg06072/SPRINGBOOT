# STOMP 채팅 서버 API 엔드포인트 정리

## 📋 목차
1. [REST API 엔드포인트](#rest-api-엔드포인트)
2. [WebSocket STOMP 엔드포인트](#websocket-stomp-엔드포인트)
3. [엔드포인트 사용 흐름](#엔드포인트-사용-흐름)

---

## 🔵 REST API 엔드포인트

### 홈 (HomeController)

| 메서드 | 엔드포인트 | 설명 | 반환 |
|--------|-----------|------|------|
| `GET` | `/` | 홈 페이지 | 채팅방 목록 페이지로 리다이렉트 (`/chat/room`) |

**예시:**
```
GET http://localhost:8080/
→ redirect: /chat/room
```

---

### 채팅방 관리 (RoomController)

#### 1. 채팅방 목록 조회
```
GET /chat/room
```
- **설명**: 채팅방 목록 페이지 반환 (Thymeleaf 템플릿)
- **반환 타입**: HTML (View)
- **반환 뷰**: `chat/room.html`
- **Response Body**: 채팅방 목록을 `rooms` 속성으로 전달

**예시:**
```
GET http://localhost:8080/chat/room
→ View: templates/chat/room.html
```

---

#### 2. 채팅방 생성
```
POST /chat/room
```
- **설명**: 새로운 채팅방 생성
- **Content-Type**: `application/x-www-form-urlencoded`
- **Request Parameter**:
  - `roomName` (String, required): 채팅방 이름
- **Response**: `ChatRoom` 객체 (JSON)
  ```json
  {
    "roomId": "uuid-string",
    "roomName": "채팅방 이름"
  }
  ```

**예시:**
```http
POST http://localhost:8080/chat/room
Content-Type: application/x-www-form-urlencoded

roomName=우리들의 채팅방
```

**Response:**
```json
{
  "roomId": "550e8400-e29b-41d4-a716-446655440000",
  "roomName": "우리들의 채팅방"
}
```

---

#### 3. 채팅방 입장
```
GET /chat/room/enter/{roomId}
```
- **설명**: 특정 채팅방 입장 페이지 반환
- **Path Variable**:
  - `roomId` (String, required): 채팅방 ID
- **반환 타입**: HTML (View)
- **반환 뷰**: `chat/roomdetail.html`
- **Response Body**: 채팅방 정보를 `room` 속성으로 전달
- **에러 처리**: 채팅방이 존재하지 않으면 `/chat/room`으로 리다이렉트

**예시:**
```
GET http://localhost:8080/chat/room/enter/550e8400-e29b-41d4-a716-446655440000
→ View: templates/chat/roomdetail.html
```

---

## 🔌 WebSocket STOMP 엔드포인트

### WebSocket 연결 엔드포인트

#### 1. STOMP 연결
```
WebSocket: /ws-stomp
```
- **설명**: WebSocket/SockJS 연결 엔드포인트
- **프로토콜**: STOMP over WebSocket (SockJS 폴백 지원)
- **클라이언트 연결 예시**:
  ```javascript
  const socket = new SockJS('/ws-stomp');
  const stompClient = Stomp.over(socket);
  stompClient.connect({}, function(frame) {
      // 연결 성공
  });
  ```

---

### 메시지 발행 엔드포인트 (클라이언트 → 서버)

#### 2. 채팅 메시지 발행
```
STOMP SEND: /pub/chat/message
```
- **설명**: 채팅 메시지를 서버로 발행 (메시지 타입: ENTER, TALK, QUIT)
- **Prefix**: `/pub` (Application Destination Prefix)
- **실제 매핑**: `@MessageMapping("/chat/message")`
- **메시지 형식** (JSON):
  ```json
  {
    "type": "ENTER | TALK | QUIT",
    "roomId": "채팅방 ID",
    "sender": "사용자 이름",
    "message": "메시지 내용"
  }
  ```

**메시지 타입별 설명:**

| 타입 | 설명 | message 필드 |
|------|------|--------------|
| `ENTER` | 채팅방 입장 | 빈 문자열 또는 무시됨 (서버에서 자동 생성) |
| `TALK` | 일반 채팅 메시지 | 실제 채팅 메시지 내용 |
| `QUIT` | 채팅방 퇴장 | 빈 문자열 또는 무시됨 (서버에서 자동 생성) |

**클라이언트 사용 예시:**
```javascript
// ENTER 메시지 발행
stompClient.send("/pub/chat/message", {}, JSON.stringify({
    type: 'ENTER',
    roomId: '550e8400-e29b-41d4-a716-446655440000',
    sender: '홍길동',
    message: ''
}));

// TALK 메시지 발행
stompClient.send("/pub/chat/message", {}, JSON.stringify({
    type: 'TALK',
    roomId: '550e8400-e29b-41d4-a716-446655440000',
    sender: '홍길동',
    message: '안녕하세요!'
}));

// QUIT 메시지 발행
stompClient.send("/pub/chat/message", {}, JSON.stringify({
    type: 'QUIT',
    roomId: '550e8400-e29b-41d4-a716-446655440000',
    sender: '홍길동',
    message: ''
}));
```

---

### 메시지 구독 엔드포인트 (서버 → 클라이언트)

#### 3. 채팅방 메시지 구독
```
STOMP SUBSCRIBE: /sub/chat/room/{roomId}
```
- **설명**: 특정 채팅방의 메시지를 구독하여 수신
- **Prefix**: `/sub` (Simple Broker Prefix)
- **Path Variable**: `{roomId}` - 채팅방 ID
- **수신 메시지 형식** (JSON):
  ```json
  {
    "type": "ENTER | TALK | QUIT",
    "roomId": "채팅방 ID",
    "sender": "사용자 이름",
    "message": "메시지 내용 (ENTER/QUIT는 서버에서 변환된 메시지)"
  }
  ```

**클라이언트 구독 예시:**
```javascript
stompClient.subscribe('/sub/chat/room/550e8400-e29b-41d4-a716-446655440000', function(message) {
    const chatMessage = JSON.parse(message.body);
    console.log('받은 메시지:', chatMessage);
    
    // ENTER 타입 예시
    // { type: 'ENTER', roomId: '...', sender: '홍길동', message: '홍길동님이 입장하셨습니다.' }
    
    // TALK 타입 예시
    // { type: 'TALK', roomId: '...', sender: '홍길동', message: '안녕하세요!' }
    
    // QUIT 타입 예시
    // { type: 'QUIT', roomId: '...', sender: '홍길동', message: '홍길동님이 퇴장하셨습니다.' }
});
```

**서버에서 메시지 전송:**
```java
// ChatController에서
messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
```

---

## 📊 엔드포인트 사용 흐름

### 1. 채팅방 생성부터 메시지 송수신까지 전체 흐름

```
[1] GET /chat/room
    → 채팅방 목록 페이지 표시

[2] POST /chat/room?roomName=우리들의채팅방
    → 채팅방 생성
    ← { "roomId": "uuid", "roomName": "우리들의채팅방" }

[3] GET /chat/room/enter/{roomId}
    → 채팅방 입장 페이지 표시

[4] WebSocket: /ws-stomp 연결
    → SockJS 연결 및 STOMP 클라이언트 초기화

[5] STOMP SUBSCRIBE: /sub/chat/room/{roomId}
    → 채팅방 메시지 구독 시작

[6] STOMP SEND: /pub/chat/message
    Body: { type: "ENTER", roomId: "...", sender: "홍길동", message: "" }
    → 입장 메시지 발행

[7] STOMP 수신: /sub/chat/room/{roomId}
    ← { type: "ENTER", message: "홍길동님이 입장하셨습니다." }
    → 입장 메시지 수신 (자신 및 다른 참여자들에게도 전달됨)

[8] STOMP SEND: /pub/chat/message
    Body: { type: "TALK", roomId: "...", sender: "홍길동", message: "안녕하세요!" }
    → 일반 채팅 메시지 발행

[9] STOMP 수신: /sub/chat/room/{roomId}
    ← { type: "TALK", sender: "홍길동", message: "안녕하세요!" }
    → 채팅 메시지 수신 (해당 채팅방의 모든 구독자에게 전달됨)

[10] STOMP SEND: /pub/chat/message
     Body: { type: "QUIT", roomId: "...", sender: "홍길동", message: "" }
     → 퇴장 메시지 발행

[11] STOMP 수신: /sub/chat/room/{roomId}
     ← { type: "QUIT", message: "홍길동님이 퇴장하셨습니다." }
     → 퇴장 메시지 수신

[12] WebSocket 연결 해제
     → disconnect() 호출
```

---

## 📝 엔드포인트 요약 테이블

### REST API

| 메서드 | 엔드포인트 | 용도 | 컨트롤러 |
|--------|-----------|------|---------|
| `GET` | `/` | 홈 → 채팅방 목록 리다이렉트 | HomeController |
| `GET` | `/chat/room` | 채팅방 목록 페이지 | RoomController |
| `POST` | `/chat/room` | 채팅방 생성 | RoomController |
| `GET` | `/chat/room/enter/{roomId}` | 채팅방 입장 페이지 | RoomController |

### WebSocket STOMP

| 타입 | 엔드포인트 | 용도 | 방향 |
|------|-----------|------|------|
| `WebSocket` | `/ws-stomp` | STOMP 연결 | 양방향 연결 |
| `SEND` | `/pub/chat/message` | 메시지 발행 (클라이언트 → 서버) | 클라이언트 → 서버 |
| `SUBSCRIBE` | `/sub/chat/room/{roomId}` | 메시지 구독 (서버 → 클라이언트) | 서버 → 클라이언트 |

---

## 🔧 설정 정보

### WebSocket 설정 (WebSocketConfig)

- **엔드포인트**: `/ws-stomp` (SockJS 지원)
- **메시지 브로커**: Simple Broker
- **구독 Prefix**: `/sub`
- **발행 Prefix**: `/pub`

### 메시지 매핑

- **클라이언트 발행 경로**: `/pub/chat/message`
- **서버 매핑**: `@MessageMapping("/chat/message")`
- **서버 전송 경로**: `/sub/chat/room/{roomId}`

---

## 💡 참고사항

1. **CORS 설정**: 현재는 별도 CORS 설정 없음 (같은 도메인에서만 접근 가능)
2. **인증/인가**: 현재는 구현되지 않음 (모든 사용자가 모든 채팅방 접근 가능)
3. **메시지 브로커**: Simple Broker 사용 (단일 서버 환경)
   - 확장 시 Redis나 RabbitMQ 등의 외부 메시지 브로커 사용 권장
4. **채팅방 저장소**: 메모리 기반 Map 사용 (서버 재시작 시 초기화)
   - 영구 저장이 필요하면 데이터베이스 연동 필요

