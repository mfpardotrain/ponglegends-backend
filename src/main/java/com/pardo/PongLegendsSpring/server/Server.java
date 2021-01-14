package com.pardo.PongLegendsSpring.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ServerEndpoint(value = "/ws")
public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port, GameState gameState) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                InputStream in = clientSocket.getInputStream();
                OutputStream out = clientSocket.getOutputStream();
                Scanner s = new Scanner(in, "UTF-8");

                try {
                    String data = s.useDelimiter("\\r\\n\\r\\n").next();
                    Matcher get = Pattern.compile("^GET").matcher(data);
                    if (get.find()) {
                        Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                        match.find();
                        byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                                + "Connection: Upgrade\r\n"
                                + "Upgrade: websocket\r\n"
                                + "Sec-WebSocket-Accept: "
                                + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                                + "\r\n\r\n").getBytes(StandardCharsets.UTF_8);
                        out.write(response, 0, response.length);
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                finally {
                    new EchoThread(clientSocket, gameState).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() throws IOException {
        clientSocket.close();
        serverSocket.close();
    }

}
