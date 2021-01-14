package com.pardo.PongLegendsSpring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class Coordinate {
    Double x;
    Double y;
    String name;
    Integer fromId;

    public Coordinate(String name, Integer fromId) {
        this.x = 0.0;
        this.y = 0.0;
        this.name = name;
        this.fromId = fromId;
    }

    public Integer yToInt() {
        return (int) Math.round(this.y);
    }

    public Integer xToInt() {
        return (int) Math.round(this.x);
    }

}
