package com.bedtime.chat_app.config;

import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.bedtime.chat_app.chat.ChatMessage;
import com.bedtime.chat_app.chat.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    

    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    
        Optional.ofNullable(headerAccessor.getSessionAttributes())
                .map(attrs -> (String) attrs.get("username"))
                .ifPresent(username -> {
                    log.info("User disconnected: {}", username);
                    var chatMessage = ChatMessage.builder()
                        .type(MessageType.LEAVE)
                        .sender(username)
                        .build();
                    messageTemplate.convertAndSend("/topic/public", chatMessage);
                });
    }
    




}
