package com.pardo.PongLegendsSpring.server;

import com.pardo.PongLegendsSpring.model.Command;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoThread extends Thread {
    private final Socket socket;
    private GameState gameState;

    public EchoThread(Socket clientSocket, GameState gameState) {
        this.socket = clientSocket;
        this.gameState = gameState;
    }

    @SneakyThrows
    public void run() {
        System.out.println("echo");
        InputStream in = this.socket.getInputStream();
        OutputStream out = this.socket.getOutputStream();

        String initialOut = gameState.getOutArray();
        if (initialOut != null) {
            byte[] outMess = broadcast(initialOut);
            out.write(outMess, 0, outMess.length);
        }

        while (true) {
            String message = decodeMessage(in);
            if (message.equals("close")) {
                System.out.print("break");
                break;
            }
            if (message.length() > 0) {
                Command command = new Command(message);
                if (command.getCommandInputList().getKey().equals("Reset")) {
                    gameState.resetGamestate();
                } else {
                    gameState = command.evaluateCommands(gameState);
                }
            }

        while (gameState.getChampionList().isUpdating()) {
            // TODO: this still only sends when running
            if (gameState.getTimeOpen() % 2000 == 0) {
                byte[] outMess = broadcast(gameState.currentStateMessage());
                out.write(outMess, 0, outMess.length);
            }

            if (in.available() > 0) {
                String checkNew = decodeMessage(in);
                if (checkNew.equals("Reset")) {
                    System.out.println("Equals reset");
                    gameState.resetGamestate();
                } else {
                    Command command = new Command(checkNew);
                    gameState = command.evaluateCommands(gameState);
                }

            }
            String outArray = gameState.getOutArray();
            if (outArray != null) {
                byte[] outMess = broadcast(outArray);
                out.write(outMess, 0, outMess.length);
            }
            Thread.sleep(10);
        }
    }

}

    public byte[] broadcast(String mess) {
        byte[] rawData = mess.getBytes();

        int frameCount = 0;
        byte[] frame = new byte[10];

        frame[0] = (byte) 129;

        if (rawData.length <= 125) {
            frame[1] = (byte) rawData.length;
            frameCount = 2;
        } else if (rawData.length >= 126 && rawData.length <= 65535) {
            frame[1] = (byte) 126;
            int len = rawData.length;
            frame[2] = (byte) ((len >> 8) & (byte) 255);
            frame[3] = (byte) (len & (byte) 255);
            frameCount = 4;
        } else {
            frame[1] = (byte) 127;
            int len = rawData.length;
            frame[2] = (byte) ((len >> 56) & (byte) 255);
            frame[3] = (byte) ((len >> 48) & (byte) 255);
            frame[4] = (byte) ((len >> 40) & (byte) 255);
            frame[5] = (byte) ((len >> 32) & (byte) 255);
            frame[6] = (byte) ((len >> 24) & (byte) 255);
            frame[7] = (byte) ((len >> 16) & (byte) 255);
            frame[8] = (byte) ((len >> 8) & (byte) 255);
            frame[9] = (byte) (len & (byte) 255);
            frameCount = 10;
        }

        int bLength = frameCount + rawData.length;

        byte[] reply = new byte[bLength];

        int bLim = 0;
        for (int i = 0; i < frameCount; i++) {
            reply[bLim] = frame[i];
            bLim++;
        }
        for (int i = 0; i < rawData.length; i++) {
            reply[bLim] = rawData[i];
            bLim++;
        }
        return reply;

    }

    private String bufferDecode(InputStream ireader) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(ireader));
        int length;
        char[] buffer = new char[10240];
        StringBuilder sb = new StringBuilder();

        try {
            while ((length = reader.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, length));
            }
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            int size = ireader.read(data);
            System.out.println(size);
            if (size == -1) return null;
            byte[] decoded = new byte[size - 6];
            byte[] key = new byte[]{data[2], data[3], data[4], data[5]};
            for (int i = 0; i < size - 6; i++) {
                decoded[i] = (byte) (data[i + 6] ^ key[i & 0x3]);
            }
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "ping";

    }

    private String decodeMessage(InputStream reader) {
        try {
            byte[] data = new byte[10240];
            int size = reader.read(data);

            if (size == -1) return null;
            byte[] decoded = new byte[size - 6];
            byte[] key = new byte[]{data[2], data[3], data[4], data[5]};
            for (int i = 0; i < size - 6; i++) {
                decoded[i] = (byte) (data[i + 6] ^ key[i & 0x3]);
            }
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "ping";
    }
}