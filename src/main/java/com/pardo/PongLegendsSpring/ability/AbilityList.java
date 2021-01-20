package com.pardo.PongLegendsSpring.ability;

import com.pardo.PongLegendsSpring.champion.Champion;
import com.pardo.PongLegendsSpring.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class AbilityList {
    private Integer fromId;
    ArrayList<Ability> activeAbility;

    public AbilityList(Integer fromId) {
        this.fromId = fromId;
        this.activeAbility = new ArrayList<>();
    }

    public boolean isUpdating() {
        return !this.activeAbility.isEmpty();
    }

    public void useAbility(String name, Coordinate targetLocation, Coordinate startingLocation, Champion castingChampion) {
        switch (name) {
            case "q": {
                Ability q = new AutoAttack("q", this.fromId, targetLocation, startingLocation);
                this.activeAbility.add(q);
                break;
            }
            case "e": {
                Ability e = new AutoAttack("e", this.fromId, targetLocation, startingLocation);
                Ability e1 = new AutoAttack("e", this.fromId, this.calcAngle(targetLocation, startingLocation, 10.0), startingLocation);
                Ability e2 = new AutoAttack("e", this.fromId, this.calcAngle(targetLocation, startingLocation, 20.0), startingLocation);
                Ability e3 = new AutoAttack("e", this.fromId, this.calcAngle(targetLocation, startingLocation, 340.0), startingLocation);
                Ability e4 = new AutoAttack("e", this.fromId, this.calcAngle(targetLocation, startingLocation, 350.0), startingLocation);
                this.activeAbility.add(e);
                this.activeAbility.add(e1);
                this.activeAbility.add(e2);
                this.activeAbility.add(e3);
                this.activeAbility.add(e4);
                break;
            }
            case "1": {
                Ability one = new AutoAttack("1", this.fromId, targetLocation, startingLocation);
                this.activeAbility.add(one);
                break;
            }
            case "3": {
                Ability three = new Snipe("3", this.fromId, targetLocation, startingLocation);
                this.activeAbility.add(three);
                break;
            }
        }
    }

    public Coordinate calcAngle(Coordinate targetLocation, Coordinate startingLocation, Double angle) {
        Double x1 = startingLocation.getX();
        Double y1 = startingLocation.getY();
        Double x2 = targetLocation.getX();
        Double y2 = targetLocation.getY();

        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        double theta = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

        double sinValue = Math.sin(Math.toRadians(theta + angle));
        double cosValue = Math.cos(Math.toRadians(theta + angle));

        double outX = cosValue * distance;
        double outY = sinValue * distance;

        return new Coordinate(x1 + outX, y1 + outY, targetLocation.getName(), targetLocation.getFromId());
    }

    public boolean onCooldown(String name) {
        Ability lastAbility = this.activeAbility.stream().filter(ability -> ability.getAbilityName().equals(name)).reduce((first, second) -> second).orElse(null);
        if (lastAbility == null) {
            return false;
        } else {
            return lastAbility.onCooldown();
        }
    }

}
