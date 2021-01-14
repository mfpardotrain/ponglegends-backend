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
    }

    public Coordinate calcDistance(Double tickRate) {
        this.prevLocation = this.location;
        Double x1 = this.location.getX();
        Double y1 = this.location.getY();
        Double x2 = this.targetLocation.getX();
        Double y2 = this.targetLocation.getY();
        Double moveRate = this.moveRate;

        double distance = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
        double totalTime = distance / moveRate;
        Double xOut = x1 + tickRate / 1000 * (x2-x1) / totalTime;
        Double yOut = y1 + tickRate / 1000 * (y2-y1) / totalTime;

        Coordinate outCoord = Coordinate.builder().x(xOut).y(yOut).name("champion").fromId(this.fromId).build();

        if (distance < (moveRate / 1000 * tickRate)) {
            this.isUpdating = false;
            outCoord = this.targetLocation;
        }
        this.setLocation(outCoord);

        return outCoord;
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
        return (this.isUpdating | abilityList.isUpdating()) && !this.isDead();
    }

}
