package com.pardo.PongLegendsSpring.ability;

import com.pardo.PongLegendsSpring.champion.Champion;

import com.pardo.PongLegendsSpring.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Ability {
    private String abilityName;
    private Coordinate location;
    private Coordinate targetLocation;
    private Double moveRate;
    private Boolean isUpdating;
    private Integer fromId;
    private Double width;
    private Double height;
    private Double cooldownTime;
    private Double cooldownDuration;
    private Double range;
    private Double castTime;
    private Double castDuration;

    public Ability(String name, Integer fromId, Coordinate targetLocation, Coordinate startingLocation) {
        this.abilityName = name;
        this.location = startingLocation;
        this.targetLocation = targetLocation;
        this.moveRate = 200.0;
        this.isUpdating = true;
        this.fromId = fromId;
        this.cooldownTime = 1.0;
        this.cooldownDuration = 1.0;
        this.range = 200.0;
        this.width = (double) 10;
        this.height = (double) 10;
        this.castTime = (double) 0;
        this.castDuration = (double) 0;
    }

    public Coordinate calcDistance(Double tickRate) {
        Double x1 = this.location.getX();
        Double y1 = this.location.getY();
        Double x2 = this.targetLocation.getX();
        Double y2 = this.targetLocation.getY();
        Double moveRate = this.getMoveRate();

        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        if (distance > range) {
            double ratio = this.range / distance;
            x2 = (1 - ratio) * x1 + ratio * x2;
            y2 = (1 - ratio) * y1 + ratio * y2;
            this.targetLocation.setX(x2);
            this.targetLocation.setY(y2);
        }
        double totalTime = distance / moveRate;
        Double xOut = x1 + tickRate / 1000 * (x2 - x1) / totalTime;
        Double yOut = y1 + tickRate / 1000 * (y2 - y1) / totalTime;

        Coordinate outCoord = Coordinate.builder().x(xOut).y(yOut).name(this.abilityName).fromId(this.fromId).build();

        if (distance < (moveRate / 1000 * tickRate)) {
            this.isUpdating = false;
            outCoord = this.targetLocation;
        }
        this.setLocation(outCoord);

        return outCoord;
    }

    public void tickCooldown(Double tickRate) {
        this.cooldownTime = this.cooldownTime + (tickRate / 1000);
    }

    public void tickCastTime(Double tickrate) {
        this.castTime = this.castTime + (tickrate / 1000);
    }

    public boolean onCooldown() {
        return this.cooldownTime < this.cooldownDuration;
    }

    public boolean atLocation() {
        return this.location.equals(this.targetLocation);
    }

    public int toInt(Double doub) {
        return (int) Math.round(doub);
    }

    public Rectangle getBounds() {
        int xMiddle = (int) Math.round(this.location.xToInt() - (this.width / 2));
        int yMiddle = (int) Math.round(this.location.yToInt() - (this.height / 2));
        return new Rectangle(xMiddle, yMiddle, this.toInt(this.width), this.toInt(this.height));
    }

    public void startCast(Champion castingChampion) {}
    public void endCast(Champion castingChampion) {}

    public Coordinate activateEffect(Champion targetChampion, Champion castingChampion) {
        Rectangle intersection = this.getBounds().intersection(targetChampion.getBounds());
        Double adjustedX = intersection.getX() + (this.width / 2);
        Double adjustedY = intersection.getY() + (this.height / 2);
        this.setLocation(new Coordinate(adjustedX, adjustedY, this.abilityName, this.fromId));
        this.setTargetLocation(this.location);
        if (targetChampion.getHealth() - 5 > 0) {
            targetChampion.setHealth(targetChampion.getHealth() - 5);
        } else {
            targetChampion.setHealth(0.0);
        }
        return new Coordinate(targetChampion.getMaxHealth(), targetChampion.getHealth(), "championHealth", targetChampion.getFromId());
    }

    public Coordinate collide() {
        this.setIsUpdating(false);
        this.setTargetLocation(this.location);
        return this.location;
    }

}

