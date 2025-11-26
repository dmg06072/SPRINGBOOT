package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 기반 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    // 클라이언트가 서버에 연결하는 WebSocket/SockJS 엔드포인트를 등록
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // '/ws-stomp'가 엔드포인트가 되며, SockJS 폴백을 허용합니다.
        // 클라이언트에서 new SockJS('/ws-stomp')로 연결합니다.
        registry.addEndpoint("/ws-stomp").withSockJS();
    }

    @Override
    // 메시지 브로커 설정
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 1. 메시지 구독(Subscription) 요청 Prefix: '/sub'
        // 브로커에게 메시지를 전달받을 주소의 Prefix를 설정합니다.
        // 예: 구독 주소 -> '/sub/chat/room/{roomId}'
        registry.enableSimpleBroker("/sub");

        // 2. 메시지 발행(Publish) 요청 Prefix: '/pub'
        // 클라이언트가 서버로 메시지를 보낼 때 사용할 Prefix를 설정합니다.
        // 예: 발행 주소 -> '/pub/chat/message'
        registry.setApplicationDestinationPrefixes("/pub");
    }
}

