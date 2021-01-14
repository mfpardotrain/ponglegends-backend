package com.pardo.PongLegendsSpring.server;

import com.google.gson.Gson;
import com.pardo.PongLegendsSpring.ability.Ability;
import com.pardo.PongLegendsSpring.champion.Champion;
import com.pardo.PongLegendsSpring.champion.ChampionList;
import com.pardo.PongLegendsSpring.model.Coordinate;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class GameState {
    private ChampionList championList;
    private String outArray;
    private Integer timeOpen = 0;

    public GameState() {
        Champion championRedOne = new Champion("red", 1, 1);
        Champion championRedTwo = new Champion("red", 2, 1);
        Champion championRedZero = new Champion("red", 0, 1);

        Champion championBlueThree = new Champion("blue", 3, 0);
        Champion championBlueFour = new Champion("blue", 4, 0);
        Champion championBlueFive = new Champion("blue", 5, 0);

        championRedZero.setLocation(Coordinate.builder().name("red").x(50.0).y(50.0).fromId(0).build());
        championRedOne.setLocation(Coordinate.builder().name("red").x(50.0).y(100.0).fromId(1).build());
        championRedTwo.setLocation(Coordinate.builder().name("red").x(50.0).y(150.0).fromId(2).build());
        championBlueThree.setLocation(Coordinate.builder().name("blue").x(500.0).y(50.0).fromId(3).build());
        championBlueFour.setLocation(Coordinate.builder().name("blue").x(500.0).y(100.0).fromId(4).build());
        championBlueFive.setLocation(Coordinate.builder().name("blue").x(500.0).y(150.0).fromId(5).build());
        this.championList = new ChampionList(championRedZero, championRedOne, championRedTwo,
                                             championBlueThree, championBlueFour, championBlueFive);
    }

    public synchronized void evaluateGamestate(double tickRate) {
        // Initialize output
        List<Coordinate> outCoord = new java.util.ArrayList<>(Collections.emptyList());
        // Get all updating champions
        List<Champion> updatingChampion = this.championList.getUpdatingChampion();

        updatingChampion.forEach(champion -> {
            List<Ability> activeAbility = champion.getActiveAbility();
            activeAbility.removeIf(ability -> (!ability.getIsUpdating() && !ability.onCooldown()));
            this.championList.toList().stream().filter(x -> !x.equals(champion)).forEach(gameChampion -> {
                if (champion.getBounds().intersects(gameChampion.getBounds())) {
                    champion.setIsUpdating(false);
                    champion.setLocation(champion.getPrevLocation());
                    outCoord.add(champion.getPrevLocation());
                }
                if (!activeAbility.isEmpty()) {
                    activeAbility.forEach(ability -> {
                        if(ability.getIsUpdating()) {
                            if (ability.getBounds().intersects(gameChampion.getBounds()) & champion.isEnemy(gameChampion)) {
                                outCoord.add(ability.activateEffect(gameChampion));
                                outCoord.add(ability.getLocation());
                                ability.setIsUpdating(false);
                            }
                        }
                    });
                }
            });
            if (champion.getIsUpdating()) {
                outCoord.add(champion.calcDistance(tickRate));
            }
            activeAbility.forEach(ability -> {
                if (!ability.atLocation()) outCoord.add(ability.calcDistance(tickRate));
                ability.tickCooldown(tickRate);
            });
        });

        this.outArray = new Gson().toJson(outCoord);
    }

    public String currentStateMessage() {
        List<Coordinate> outCoord = new java.util.ArrayList<>(Collections.emptyList());
        this.championList.toList().forEach(champion -> {
            outCoord.add(champion.getLocation());
            outCoord.add(new Coordinate(champion.getMaxHealth(), champion.getHealth(), "championHealth", champion.getFromId()));
        });
        return new Gson().toJson(outCoord);
    }

    public void resetGamestate() {
        Champion championRedOne = new Champion("red", 1, 1);
        Champion championRedTwo = new Champion("red", 2, 1);
        Champion championRedZero = new Champion("red", 0, 1);

        Champion championBlueThree = new Champion("blue", 3, 0);
        Champion championBlueFour = new Champion("blue", 4, 0);
        Champion championBlueFive = new Champion("blue", 5, 0);

        championRedZero.setLocation(Coordinate.builder().name("champion").x(50.0).y(50.0).fromId(0).build());
        championRedOne.setLocation(Coordinate.builder().name("champion").x(50.0).y(100.0).fromId(1).build());
        championRedTwo.setLocation(Coordinate.builder().name("champion").x(50.0).y(150.0).fromId(2).build());
        championBlueThree.setLocation(Coordinate.builder().name("champion").x(500.0).y(50.0).fromId(3).build());
        championBlueFour.setLocation(Coordinate.builder().name("champion").x(500.0).y(100.0).fromId(4).build());
        championBlueFive.setLocation(Coordinate.builder().name("champion").x(500.0).y(150.0).fromId(5).build());
        this.championList = new ChampionList(championRedZero, championRedOne, championRedTwo,
                championBlueThree, championBlueFour, championBlueFive);
    }

}
