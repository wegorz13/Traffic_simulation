package model;

public record TrafficFlow (MoveDirection fromDirection, MoveDirection toDirection){

    public TrafficFlow {
        if (fromDirection == null || toDirection == null || fromDirection == toDirection) {
            throw new IllegalArgumentException("Directions must not be null");
        }
    }

    @Override
    public String toString() {
        return fromDirection + " -> " + toDirection;
    }

}
