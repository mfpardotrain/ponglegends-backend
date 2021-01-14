package com.pardo.PongLegendsSpring;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;

public class WebSocketHandler extends AbstractWebSocketHandler {
    Main main = null;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException, InterruptedException {
        if (this.main == null) {
            this.main = new Main(session);
        }
        System.out.println("Message Received");
        this.main.recieveMessage(message.getPayload());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        System.out.println("New Binary Message Received");
        session.sendMessage(message);
    }

}