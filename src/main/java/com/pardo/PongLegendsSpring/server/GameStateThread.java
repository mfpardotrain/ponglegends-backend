package com.pardo.PongLegendsSpring.server;

import com.pardo.PongLegendsSpring.champion.Champion;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
public class GameStateThread extends Thread {
    private GameState gameState;

    public GameStateThread(GameState gameState) {
        this.gameState = gameState;
    }

    @SneakyThrows
    public void run() {
        Integer tickRate = 10;
        while (true) {

            List<Champion> deadChampion = this.gameState.getChampionList().getAllDead();
            List<Champion> team0Dead = deadChampion.stream().filter(champion -> champion.getTeam().equals(0)).collect(Collectors.toList());
            List<Champion> team1Dead = deadChampion.stream().filter(champion -> champion.getTeam().equals(1)).collect(Collectors.toList());
            if (team0Dead.size() == 3) {
                this.gameState.resetGamestate();
            }
            if (team1Dead.size() == 3) {
                this.gameState.resetGamestate();
            }
            gameState.setTimeOpen(gameState.getTimeOpen() + tickRate);
            gameState.evaluateGamestate(tickRate);
            Thread.sleep(tickRate);
        }
    }

}
