package com.pardo.PongLegendsSpring.ability;

import com.pardo.PongLegendsSpring.champion.Champion;
import com.pardo.PongLegendsSpring.model.Coordinate;
import lombok.Data;

import java.awt.*;

@Data
public class Snipe extends Ability {
    private Double prevMoveRate;

    public Snipe(String name, Integer fromId, Coordinate targetLocation, Coordinate startingLocation) {
        super(name, fromId, targetLocation, startingLocation);
        this.setCooldownTime(5.0);
        this.setMoveRate(500.0);
        this.setRange(1000.0);
        this.setWidth(3.0);
        this.setHeight(3.0);
        this.setCastDuration(1.0);
    }

    public void startCast(Champion castingChampion) {
        castingChampion.setCanMove(false);
        castingChampion.setXSpeed(0.0);
        castingChampion.setYSpeed(0.0);
    }

    public void endCast(Champion castingChampion) {
        castingChampion.setCanMove(true);
    }

    public Coordinate activateEffect(Champion targetChampion, Champion castingChampion) {
        Rectangle intersection = this.getBounds().intersection(targetChampion.getBounds());
        Double adjustedX = intersection.getX() + (this.getWidth() / 2);
        Double adjustedY = intersection.getY() + (this.getWidth() / 2);
        this.setLocation(new Coordinate(adjustedX, adjustedY, this.getAbilityName(), this.getFromId()));
        this.setTargetLocation(this.getLocation());
        if (targetChampion.getHealth() - 30 > 0) {
            targetChampion.setHealth(targetChampion.getHealth() - 30);
        } else {
            targetChampion.setHealth(0.0);
        }
        return new Coordinate(targetChampion.getMaxHealth(), targetChampion.getHealth(), "championHealth", targetChampion.getFromId());
    }

}
