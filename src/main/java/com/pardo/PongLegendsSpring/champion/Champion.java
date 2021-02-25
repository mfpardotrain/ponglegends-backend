package com.pardo.PongLegendsSpring.champion;

import com.pardo.PongLegendsSpring.ability.Ability;
import com.pardo.PongLegendsSpring.ability.AbilityList;
import com.pardo.PongLegendsSpring.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@Builder
public class Champion {
    private String championName;
    private Coordinate location;
    private Coordinate targetLocation;
    private Integer fromId;
    private Double maxSpeed;
    private Double xSpeed;
    private Double ySpeed;
    private Double xAcceleration;
    private Double yAcceleration;
    private Boolean isUpdating;
    private Double width;
    private Double height;
    private AbilityList abilityList;
    private ArrayList<Ability> effectList;
    private Coordinate prevLocation;
    private Integer team;
    private Double health;
    private Double maxHealth;
    private Double up;
    private Double down;
    private Double left;
    private Double right;
    private Boolean canMove;
    private Boolean flipRotation;

    public Champion(String name, Integer fromId, Integer team) {
        this.championName = name;
        this.location = Coordinate.builder().x(100.0).y(100.0).name("champion").build();
        this.targetLocation = Coordinate.builder().x(100.0).y(100.0).name("champion").build();
        this.prevLocation = this.location;
        this.fromId = fromId;
        this.maxSpeed = 200.0;
        this.xSpeed = 0.0;
        this.ySpeed = 0.0;
        this.xAcceleration = 0.5;
        this.yAcceleration = 0.5;
        this.isUpdating = false;
        this.width = 30.0;
        this.height = 30.0;
        this.abilityList = new AbilityList(fromId);
        this.effectList = new ArrayList<>();
        this.team = team;
        this.health = 100.0;
        this.maxHealth = 100.0;
        this.up = 0.0;
        this.down = 0.0;
        this.left = 0.0;
        this.right = 0.0;
        this.canMove = true;
        this.flipRotation = false;
    }

    public Coordinate calcDistance(Double tickRate) {
        Coordinate out = new Coordinate();
        if (isMoving()) {
            out = move(tickRate);
        }
        return out;
    }

    public boolean isMoving() {
        return this.up.equals(1.0) || this.down.equals(1.0) || this.left.equals(1.0) || this.right.equals(1.0) || this.xSpeed != 0 || this.ySpeed != 0;
    }

    public Coordinate move(Double tickRate) {
        this.prevLocation = this.location;
        Double x1 = this.location.getX();
        Double y1 = this.location.getY();

        if (this.up.equals(1.0)) {
            if (this.ySpeed > -this.maxSpeed) {
                this.ySpeed = this.ySpeed - this.yAcceleration;
            }
        }
        if (this.down.equals(1.0)) {
            if (this.ySpeed < this.maxSpeed) {
                this.ySpeed = this.ySpeed + this.yAcceleration;
            }
        }
        if (this.left.equals(1.0)) {
            if (this.xSpeed > -this.maxSpeed) {
                this.xSpeed = this.xSpeed - this.xAcceleration;
            }
        }
        if (this.right.equals(1.0)) {
            if (this.xSpeed < this.maxSpeed) {
                this.xSpeed = this.xSpeed + this.xAcceleration;
            }
        }

        // this.decelerate(this.xAcceleration, this.yAcceleration);

        Double yOut = y1 + tickRate * this.ySpeed / 1000;
        Double xOut = x1 + tickRate * this.xSpeed / 1000;

        Coordinate outCoord = Coordinate.builder().x(xOut).y(yOut).name("champion").fromId(this.fromId).build();
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

    public void bounceTerrain(Area object) {
        int xMiddle = (int) Math.round(this.location.xToInt() - (this.width / 2));
        int yMiddle = (int) Math.round(this.location.yToInt() - (this.height / 2));
        Rectangle xRect = new Rectangle(xMiddle, yMiddle + 2, this.toInt(this.width), this.toInt(this.height) - 4);
        Rectangle yRect = new Rectangle(xMiddle + 2, yMiddle, this.toInt(this.width) - 4, this.toInt(this.height));

        if (!object.contains(yRect)) {
            this.ySpeed = this.ySpeed * -1;
        }
        if (!object.contains(xRect)) {
            this.xSpeed = this.xSpeed * -1;
        }
        this.flipRotation = !this.flipRotation;
    }

    public void bounceChampion(Rectangle object) {
        int xMiddle = (int) Math.round(this.location.xToInt() - (this.width / 2));
        int yMiddle = (int) Math.round(this.location.yToInt() - (this.height / 2));
        Rectangle xRect = new Rectangle(xMiddle, yMiddle + 4, this.toInt(this.width), this.toInt(this.height) - 4);
        Rectangle yRect = new Rectangle(xMiddle + 4, yMiddle, this.toInt(this.width) - 4, this.toInt(this.height));

        if (object.intersects(yRect)) {
            this.ySpeed = this.ySpeed * -1;
        }
        if (object.intersects(xRect)) {
            this.xSpeed = this.xSpeed * -1;
        }
        this.flipRotation = !this.flipRotation;
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

    public Coordinate collide() {
        this.setIsUpdating(false);
        this.setLocation(this.getPrevLocation());
        return this.prevLocation;
    }

    public void decelerate(Double xDecel, Double yDecel) {

        if (this.right.equals(0.0) && this.left.equals(0.0)) {
            if (this.xSpeed > 0) {
                if (this.xSpeed - xDecel < 0) {
                    this.xSpeed = 0.0;
                } else {
                    this.xSpeed = this.xSpeed - xDecel;
                }
            }
            if (this.xSpeed < 0) {
                if (this.xSpeed + xDecel > 0) {
                    this.xSpeed = 0.0;
                } else {
                    this.xSpeed = this.xSpeed + xDecel;
                }
            }
        }

        if (this.up.equals(0.0) && this.down.equals(0.0)) {
            if (this.ySpeed > 0) {
                if (this.ySpeed - yDecel < 0) {
                    this.ySpeed = 0.0;
                } else {
                    this.ySpeed = this.ySpeed - yDecel;
                }
            }
            if (this.ySpeed < 0) {
                if (this.ySpeed + yDecel > 0) {
                    this.ySpeed = 0.0;
                } else {
                    this.ySpeed = this.ySpeed + yDecel;
                }
            }
        }

    }

}
