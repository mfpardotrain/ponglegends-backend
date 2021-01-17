package com.pardo.PongLegendsSpring.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.pardo.PongLegendsSpring.champion.Champion;
import com.pardo.PongLegendsSpring.champion.ChampionList;
import com.pardo.PongLegendsSpring.server.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.StringReader;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {
    private String commandJson;
    private CommandInputList commandInputList;

    public Command(String commandJson) {
        try {
            // TODO: Does not completely fix the malformed json
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(commandJson));
            reader.setLenient(true);
            this.commandInputList = gson.fromJson(reader, CommandInputList.class);
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

