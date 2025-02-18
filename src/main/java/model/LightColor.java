package model;

import javafx.scene.paint.Color;

public enum LightColor {
    RED,
    YELLOW,
    GREEN;

    public LightColor previous(){
        return switch (this){
            case GREEN ->  LightColor.YELLOW;
            case YELLOW, RED ->  LightColor.RED;
        };
    }

    public LightColor next(){
        return switch (this){
            case RED ->  LightColor.YELLOW;
            case YELLOW, GREEN ->  LightColor.GREEN;
        };
    }

    public Color toPaintColor(){
        return switch (this){
            case RED -> Color.RED;
            case YELLOW -> Color.YELLOW;
            case GREEN -> Color.GREEN;
        };
    }
}
