package com.pardo.PongLegendsSpring.ability;

import com.pardo.PongLegendsSpring.champion.Champion;
import com.pardo.PongLegendsSpring.model.Coordinate;
import lombok.Data;

import java.awt.*;

@Data
public class Snipe extends Ability {

    public Snipe(String name, Integer fromId, Coordinate targetLocation, Coordinate startingLocation) {
        super(name, fromId, targetLocation, startingLocation);
        this.setMoveRate(500.0);
        this.setRange(1000.0);
        this.setWidth(3.0);
        this.setHeight(3.0);
        this.setCastDuration(1.0);
        this.setCastTime(0.0);
        this.setCooldownTime(0.0);
        this.setCooldownDuration(3.0);
    }

    public void startCast(Champion castingChampion) {
        castingChampion.setCanMove(false);
        castingChampion.setXSpeed(0.0);
        castingChampion.setYSpeed(0.0);
    }

    public void endCast(Champion castingChampion) {
        castingChampion.setCanMove(true);
        Push recoil = new Push("none", castingChampion.getFromId(), this.getTargetLocation(), castingChampion.getLocation());
        castingChampion.getEffectList().add(recoil);
        this.setHasCasted(true);
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
