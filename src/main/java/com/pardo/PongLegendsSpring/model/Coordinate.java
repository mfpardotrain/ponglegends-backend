package com.pardo.PongLegendsSpring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coordinate {
    Double x;
    Double y;
    String name;
    Integer fromId;

    public Integer yToInt() {
        return (int) Math.round(this.y);
    }

    public Integer xToInt() {
        return (int) Math.round(this.x);
    }

}
