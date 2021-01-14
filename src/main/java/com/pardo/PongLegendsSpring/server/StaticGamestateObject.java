package com.pardo.PongLegendsSpring.server;

import lombok.Data;

import javax.swing.*;
import java.awt.*;

@Data
public class StaticGamestateObject {
    private JFrame frame;

    public void StaticGamestateObject() {
        this.frame = new JFrame("Pong Legends");
        this.frame.setLayout(new BorderLayout());
        this.frame.setBounds(new Rectangle(0, 0, 1000, 1000));
        System.out.println(frame.getBounds());

    }
}
