package com.pardo.PongLegendsSpring.champion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChampionList {
    Champion championZero;
    Champion championOne;
    Champion championTwo;
    Champion championThree;
    Champion championFour;
    Champion championFive;

    public Champion findChampion(Integer fromId) {
        if (championZero.getFromId().equals(fromId)) {
            return championZero;
        }
        if (championOne.getFromId().equals(fromId)) {
            return championOne;
        }
        if (championTwo.getFromId().equals(fromId)) {
            return championTwo;
        }
        if (championThree.getFromId().equals(fromId)) {
            return championThree;
        }
        if (championFour.getFromId().equals(fromId)) {
            return championFour;
        }
        if (championFive.getFromId().equals(fromId)) {
            return championFive;
        }
        else {
            System.out.println("No champion with that fromId");
            return null;
        }
    }

    public List<Champion> getUpdatingChampion() {
        List<Champion> outList = new java.util.ArrayList<>(Collections.emptyList());
        if (championZero.getAllIsUpdating()) {
            outList.add(championZero);
        }
        if (championOne.getAllIsUpdating()) {
            outList.add(championOne);
        }
        if (championTwo.getAllIsUpdating()) {
            outList.add(championTwo);
        }
        if (championThree.getAllIsUpdating()) {
            outList.add(championThree);
        }
        if (championFour.getAllIsUpdating()) {
            outList.add(championFour);
        }
        if (championFive.getAllIsUpdating()) {
            outList.add(championFive);
        }
        return outList;
    }

    public List<Champion> getAllDead() {
        List<Champion> outList = new java.util.ArrayList<>(Collections.emptyList());
        if (championZero.isDead()) {
            outList.add(championZero);
        }
        if (championOne.isDead()) {
            outList.add(championOne);
        }
        if (championTwo.isDead()) {
            outList.add(championTwo);
        }
        if (championThree.isDead()) {
            outList.add(championThree);
        }
        if (championFour.isDead()) {
            outList.add(championFour);
        }
        if (championFive.isDead()) {
            outList.add(championFive);
        }
        return outList;
    }

    public List<Champion> toList() {
        return Arrays.asList(this.championOne, this.championZero, this.championTwo,
                this.championThree, this.championFour, this.championFive);
    }

    public boolean isUpdating() {
        return (championZero.getAllIsUpdating() | championOne.getAllIsUpdating() | championTwo.getAllIsUpdating() |
                championThree.getAllIsUpdating() | championFour.getAllIsUpdating() | championFive.getAllIsUpdating());
    }
}
