package com.pardo.PongLegendsSpring.ability;

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

    public void useAbility(String name, Coordinate targetLocation, Coordinate startingLocation) {
        switch (name) {
            case "q": {
                Ability q = new Ability("q", this.fromId, targetLocation, startingLocation);
                q.setCooldownTime((double) 0);
                this.activeAbility.add(q);
                break;
            }
            case "e": {
                Ability e = new Ability("e", this.fromId, targetLocation, startingLocation);
                e.setCooldownTime((double) 0);
                this.activeAbility.add(e);
                break;
            }
            case "1": {
                Ability one = new Ability("1", this.fromId, targetLocation, startingLocation);
                one.setCooldownTime((double) 0);
                this.activeAbility.add(one);
                break;
            }
            case "3": {
                Ability three = new Ability("3", this.fromId, targetLocation, startingLocation);
                three.setCooldownTime((double) 0);
                this.activeAbility.add(three);
                break;
            }
        }
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
