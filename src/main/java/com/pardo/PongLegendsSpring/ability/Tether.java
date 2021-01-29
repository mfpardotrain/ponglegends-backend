package com.pardo.PongLegendsSpring.ability;

import com.pardo.PongLegendsSpring.champion.Champion;
import com.pardo.PongLegendsSpring.model.Coordinate;
import lombok.Data;

import java.awt.*;

@Data
public class Tether extends Ability {
    Boolean isTethered;
    Double length;
    Double prevXAccel;
    Double prevYAccel;

    public Tether(String name, Integer fromId, Coordinate targetLocation, Coordinate startingLocation) {
        super(name, fromId, targetLocation, startingLocation);
        this.setMoveRate(500.0);
        this.setRange(300.0);
        this.setWidth(1.0);
        this.setHeight(1.0);
        this.setCooldownTime(0.0);
        this.setCooldownDuration(0.0);
        this.isTethered = true;
        this.length = 0.0;
        this.prevXAccel = 0.0;
        this.prevYAccel = 0.0;
    }

    public Coordinate calcDistance(Double tickRate) {
        Double x1 = this.getLocation().getX();
        Double y1 = this.getLocation().getY();
        Double x2 = this.getTargetLocation().getX();
        Double y2 = this.getTargetLocation().getY();
        Double moveRate = this.getMoveRate();

        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        if (distance > this.getRange()) {
            double ratio = this.getRange() / distance;
            x2 = (1 - ratio) * x1 + ratio * x2;
            y2 = (1 - ratio) * y1 + ratio * y2;
            this.getTargetLocation().setX(x2);
            this.getTargetLocation().setY(y2);
        }
        double totalTime = distance / moveRate;
        Double xOut = x1 + tickRate / 1000 * (x2 - x1) / totalTime;
        Double yOut = y1 + tickRate / 1000 * (y2 - y1) / totalTime;

        Coordinate outCoord = Coordinate.builder().x(xOut).y(yOut).name(this.getAbilityName()).fromId(this.getFromId()).build();

        if (distance < (moveRate / 1000 * tickRate)) {
            outCoord = this.getTargetLocation();
        }
        this.setLocation(outCoord);

        return outCoord;
    }

    public Coordinate activateEffect(Champion targetChampion, Champion castingChampion) {
        firstCast(targetChampion, castingChampion);
        return this.getLocation();
    }

    public void firstCast(Champion targetChampion, Champion castingChampion) {
        this.setLocation(new Coordinate(targetChampion.getLocation().getX(), targetChampion.getLocation().getY(), this.getAbilityName(), this.getFromId()));
        this.setTargetLocation(this.getLocation());

        Double x1 = castingChampion.getLocation().getX();
        Double y1 = castingChampion.getLocation().getY();
        Double x2 = targetChampion.getLocation().getX();
        Double y2 = targetChampion.getLocation().getY();

        if (this.prevXAccel == 0) {
            double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
            this.setLength(distance);
            this.setIsTethered(true);
            this.setIsUpdating(true);
            this.prevXAccel = castingChampion.getXAcceleration();
            this.prevYAccel = castingChampion.getYAcceleration();

            double ySpeed = -1 * (y2 - y1);
            double xSpeed = -1 * (x2 - x1);
            castingChampion.setXSpeed(ySpeed);
            castingChampion.setYSpeed(xSpeed);
        }

        double accelMag = 20.0 * 10.0 / this.length;
        double theta = -1 * Math.atan2(y1 - y2, x1 - x2);

        double xAccel = -1 * Math.cos(theta) * accelMag;
        double yAccel = Math.sin(theta) * accelMag;

        double theta2 = Math.atan2(y2 - y1, x2 - x1);
        double xPos = -1 * (Math.cos(theta2) * this.length) + targetChampion.getLocation().getX();
        double yPos = -1 * (Math.sin(theta2) * this.length) + targetChampion.getLocation().getY();
        castingChampion.setLocation(new Coordinate(xPos, yPos, castingChampion.getChampionName(), castingChampion.getFromId()));

        System.out.println("theta " + Math.toDegrees(theta));

        castingChampion.setXSpeed(castingChampion.getXSpeed() + xAccel);
        castingChampion.setYSpeed(castingChampion.getYSpeed() + yAccel);

    }

    public void secondCast(Champion castingChampion) {
        castingChampion.setXAcceleration(this.prevXAccel);
        castingChampion.setYAcceleration(this.prevYAccel);
        setIsTethered(false);
        setIsUpdating(false);
    }

    public Boolean atLocation() {
        if (isTethered) {
            return false;
        }
        if (!isTethered) {
            return true;
        }
        return false;
    }

    public Boolean getIsAbilityUpdating() {
        return this.getIsUpdating();
    }

}
