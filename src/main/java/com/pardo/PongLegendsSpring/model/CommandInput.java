package com.pardo.PongLegendsSpring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandInput {
    private String key;
    private Coordinate coordinate;

}
