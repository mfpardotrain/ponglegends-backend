package com.pardo.PongLegendsSpring;

import com.pardo.PongLegendsSpring.server.GameState;
import com.pardo.PongLegendsSpring.server.GameStateThread;
import com.pardo.PongLegendsSpring.server.Server;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;

@Data
public class Main {
    private WebSocketSession session;
    private String message;
    private GameState gameState;

    public Main(WebSocketSession session) {
        this.session = session;
        this.message = null;
        this.gameState = new GameState();
    }

    public void recieveMessage(String message) throws IOException {
        GameStateThread GST = null;
        System.out.println(message);
        if (this.message == null) {
            this.gameState = new GameState();
            Server server = new Server();
            GST = new GameStateThread(this.gameState);
            GST.start();
            server.start(8082, this.gameState);
        }

        this.message = message;
    }
}
