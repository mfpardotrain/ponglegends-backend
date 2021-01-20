package com.pardo.PongLegendsSpring.ability;

import com.pardo.PongLegendsSpring.champion.Champion;
import com.pardo.PongLegendsSpring.model.Coordinate;
import lombok.Data;

@Data
public class Push extends Ability {
    Double prevXAccel;
    Double prevYAccel;

    public Push(String name, Integer fromId, Coordinate targetLocation, Coordinate startingLocation) {
        super(name, fromId, targetLocation, startingLocation);
        this.setCooldownDuration(0.0);
        this.setCastDuration(1.0);
        this.setCastTime(0.0);
        this.setCooldownTime(0.0);
    }

    public void startCast(Champion castingChampion) {
        this.prevXAccel = castingChampion.getXAcceleration();
        this.prevYAccel = castingChampion.getYAcceleration();
        this.activate(castingChampion);
        this.setIsUpdating(true);
    }

    public void endCast(Champion castingChampion) {
        this.setHasCasted(true);
        castingChampion.setXAcceleration(this.prevXAccel);
        castingChampion.setYAcceleration(this.prevYAccel);
        this.setIsUpdating(false);
    }

    public void activate(Champion castingChampion) {
        Double x1 = this.getLocation().getX();
        Double y1 = this.getLocation().getY();
        Double x2 = this.getTargetLocation().getX();
        Double y2 = this.getTargetLocation().getY();

        double theta = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

        double sinValue = Math.sin(Math.toRadians(theta));
        double cosValue = Math.cos(Math.toRadians(theta));

        double outX = cosValue * -200;
        double outY = sinValue * -200;

        castingChampion.setXAcceleration(Math.abs(outX) / 50);
        castingChampion.setYAcceleration(Math.abs(outY) / 50);
        castingChampion.setXSpeed(outX);
        castingChampion.setYSpeed(outY);

    }
}
