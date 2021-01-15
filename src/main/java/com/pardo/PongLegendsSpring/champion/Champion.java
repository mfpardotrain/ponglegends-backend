package com.pardo.PongLegendsSpring.champion;

import com.pardo.PongLegendsSpring.ability.Ability;
import com.pardo.PongLegendsSpring.ability.AbilityList;
import com.pardo.PongLegendsSpring.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@Builder
public class Champion {
    private String championName;
    private Coordinate location;
    private Coordinate targetLocation;
    private Integer fromId;
    private Double moveRate;
    private Boolean isUpdating;
    private Double width;
    private Double height;
    private AbilityList abilityList;
    private Coordinate prevLocation;
    private Integer team;
    private Double health;
    private Double maxHealth;
    private Double up;
    private Double down;
    private Double left;
    private Double right;

    public Champion(String name, Integer fromId, Integer team) {
        this.championName = name;
        this.location = Coordinate.builder().x(100.0).y(100.0).name("champion").build();
        this.targetLocation = Coordinate.builder().x(100.0).y(100.0).name("champion").build();
        this.prevLocation = this.location;
        this.fromId = fromId;
        this.moveRate = 100.0;
        this.isUpdating = false;
        this.width = (double) 30;
        this.height = (double) 30;
        this.abilityList = new AbilityList(fromId);
        this.team = team;
        this.health = 100.0;
        this.maxHealth = 100.0;
        this.up = 0.0;
        this.down = 0.0;
        this.left = 0.0;
        this.right = 0.0;
    }

    public Coordinate calcDistance(Double tickRate) {
        this.prevLocation = this.location;
        Double x1 = this.location.getX();
        Double y1 = this.location.getY();
        Double moveRate = this.moveRate;

        Double xOut = x1;
        Double yOut = y1;

        if (this.up.equals(1.0)) {
            yOut = y1 - tickRate * moveRate / 1000;
        }
        if (this.down.equals(1.0)) {
            yOut = y1 + tickRate * moveRate / 1000;
        }
        if (this.left.equals(1.0)) {
            xOut = x1 - tickRate * moveRate / 1000;
        }
        if (this.right.equals(1.0)) {
            xOut = x1 + tickRate * moveRate / 1000;
        }

        Coordinate outCoord = Coordinate.builder().x(xOut).y(yOut).name("champion").fromId(this.fromId).build();
        this.setLocation(outCoord);

        return outCoord;
    }

    public boolean isMoving() {
        return this.up.equals(1.0) || this.down.equals(1.0) || this.left.equals(1.0) || this.right.equals(1.0);
    }

    public boolean isEnemy(Champion champion) {
        return !this.team.equals(champion.team);
    }

    public int toInt(Double doub) {
        return (int) Math.round(doub);
    }

    public Rectangle getBounds() {
        int xMiddle = (int) Math.round(this.location.xToInt() - (this.width / 2));
        int yMiddle = (int) Math.round(this.location.yToInt() - (this.height / 2));
        return new Rectangle(xMiddle, yMiddle, this.toInt(this.width), this.toInt(this.height));
    }

    public Boolean isDead() {
        return this.health <= 0;
    }

    public ArrayList<Ability> getActiveAbility() {
        return this.abilityList.getActiveAbility();
    }

    public boolean getAllIsUpdating() {
        return (this.isMoving() || this.isUpdating || abilityList.isUpdating()) && !this.isDead();
    }

}
