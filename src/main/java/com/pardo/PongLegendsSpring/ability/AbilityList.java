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
        this.activeAbility = new ArrayList<Ability>();
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
            case "w": {
                Ability w = new Ability("w", this.fromId, targetLocation, startingLocation);
                w.setCooldownTime((double) 0);
                this.activeAbility.add(w);
                break;
            }
            case "e": {
                Ability e = new Ability("e", this.fromId, targetLocation, startingLocation);
                e.setCooldownTime((double) 0);
                this.activeAbility.add(e);
                break;
            }
            case "r": {
                Ability r = new Ability("r", this.fromId, targetLocation, startingLocation);
                r.setCooldownTime((double) 0);
                this.activeAbility.add(r);
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
