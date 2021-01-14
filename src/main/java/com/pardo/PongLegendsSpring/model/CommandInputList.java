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
            case "3": {
                Coordinate targetCoordinate = Coordinate.builder()
                        .x(coordinate.getX())
                        .y(coordinate.getY())
                        .name("champion")
                        .fromId(champion.getFromId())
                        .build();
                champion.setTargetLocation(targetCoordinate);
                champion.setIsUpdating(true);
                break;
            }
            case "q":
                this.executeAbility("q", champion);
                break;
            case "w":
                this.executeAbility("w", champion);
                break;
            case "e":
                this.executeAbility("e", champion);
                break;
            case "r":
                this.executeAbility("r", champion);
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
            abilityList.useAbility(name, targetCoordinate, champion.getLocation());
        }
    }
}
