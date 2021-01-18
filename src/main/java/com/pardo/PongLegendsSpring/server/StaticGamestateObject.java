package com.pardo.PongLegendsSpring.server;

import lombok.Data;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

@Data
public class StaticGamestateObject {
    private GeneralPath path;
    private Area terrain;

    public StaticGamestateObject() {
        int[] xPoints = {0, 0, 1000, 1000, 0};
        int[] yPoints = {0, 1000, 1000, 0, 0};
        this.path = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
                xPoints.length);

        this.path.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < xPoints.length; i++) {
            this.path.lineTo(xPoints[i], yPoints[i]);
        }

        this.terrain = new Area();
        this.terrain.add(new Area(new Rectangle(0, 0, 1000, 1000)));
        this.terrain.subtract(new Area(new Rectangle(400, 50, 50, 50)));

        this.path.closePath();

        System.out.println(this.terrain.getBounds());
    }
}
