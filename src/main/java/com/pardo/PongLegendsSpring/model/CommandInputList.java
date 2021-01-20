package com.pardo.PongLegendsSpring.model;

import com.pardo.PongLegendsSpring.ability.AbilityList;
import com.pardo.PongLegendsSpring.champion.Champion;
import lombok.Data;

@Data
public class CommandInputList {
    private Integer fromId;
    private String key;
    private Coordinate coordinate;

    public void executeInput(Champion champion) {
        switch (key) {
            // Movement
            case "w":
                champion.setUp(coordinate.getX());
                if (coordinate.getX().equals(1.0)) {
                    champion.setDown(0.0);
                }
                break;
            case "s":
                champion.setDown(coordinate.getX());
                if (coordinate.getX().equals(1.0)) {
                    champion.setUp(0.0);
                }
                break;
            case "a":
                champion.setLeft(coordinate.getX());
                if (coordinate.getX().equals(1.0)) {
                    champion.setRight(0.0);
                }
                break;
            case "d":
                champion.setRight(coordinate.getX());
                if (coordinate.getX().equals(1.0)) {
                    champion.setLeft(0.0);
                }
                break;

            // Abilities
            case "q":
                this.executeAbility("q", champion);
                break;
            case "e":
                this.executeAbility("e", champion);
                break;
            case "1":
                this.executeAbility("1", champion);
                break;
            case "3":
                this.executeAbility("3", champion);
                break;
        }
    }

    public void executeAbility(String name, Champion champion) {
        Coordinate targetCoordinate = Coordinate.builder()
                .x(coordinate.getX())
                .y(coordinate.getY())
                .name(name)
                .fromId(champion.getFromId())
                .build();
        AbilityList abilityList = champion.getAbilityList();
        if (!abilityList.onCooldown(name)) {
            abilityList.useAbility(name, targetCoordinate, champion.getLocation(), champion);
        }
    }

}
