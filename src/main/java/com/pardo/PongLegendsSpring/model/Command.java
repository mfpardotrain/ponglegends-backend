package com.pardo.PongLegendsSpring.model;

import com.google.gson.Gson;
import com.pardo.PongLegendsSpring.champion.Champion;
import com.pardo.PongLegendsSpring.champion.ChampionList;
import com.pardo.PongLegendsSpring.server.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {
    private String commandJson;
    private CommandInputList commandInputList;

    public Command(String commandJson) {
        try {
            this.commandInputList = new Gson().fromJson(commandJson, CommandInputList.class);
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public GameState evaluateCommands(GameState gameState) {
        Integer fromId = this.commandInputList.getFromId();
        ChampionList championList = gameState.getChampionList();

        Champion championToCommand = championList.findChampion(fromId);

        if(this.commandInputList.getKey() == null) {
            return gameState;
        } else {
            this.commandInputList.executeInput(championToCommand);
        }
        return gameState;
    }

}

