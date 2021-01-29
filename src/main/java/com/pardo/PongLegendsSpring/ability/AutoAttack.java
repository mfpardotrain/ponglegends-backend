package com.pardo.PongLegendsSpring.ability;

import com.pardo.PongLegendsSpring.champion.Champion;
import com.pardo.PongLegendsSpring.model.Coordinate;
import lombok.Data;

import java.awt.*;

@Data
public class AutoAttack extends Ability {

    public AutoAttack(String name, Integer fromId, Coordinate targetLocation, Coordinate startingLocation) {
        super(name, fromId, targetLocation, startingLocation);
        this.setCastTime(0.0);
        this.setCooldownTime(0.0);
        this.setCooldownDuration(1.0);
    }

    public Coordinate activateEffect(Champion targetChampion, Champion castingChampion) {
        Rectangle intersection = this.getBounds().intersection(targetChampion.getBounds());
        Double adjustedX = intersection.getX() + (this.getWidth() / 2);
        Double adjustedY = intersection.getY() + (this.getWidth() / 2);
        this.setLocation(new Coordinate(adjustedX, adjustedY, this.getAbilityName(), this.getFromId()));
        this.setTargetLocation(this.getLocation());
        this.setIsUpdating(false);
        if (targetChampion.getHealth() - 5 > 0) {
            targetChampion.setHealth(targetChampion.getHealth() - 5);
        } else {
            targetChampion.setHealth(0.0);
        }
        return new Coordinate(targetChampion.getMaxHealth(), targetChampion.getHealth(), "championHealth", targetChampion.getFromId());
    }
}
