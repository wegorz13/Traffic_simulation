package model;

public class TrafficFlow {
    private final MoveDirection fromDirection;
    private final MoveDirection toDirection;

    public TrafficFlow(MoveDirection fromDirection, MoveDirection toDirection) {
        this.fromDirection = fromDirection;
        this.toDirection = toDirection;
    }

    public MoveDirection getFromDirection() {
        return fromDirection;
    }

    @Override
    public String toString() {
        return fromDirection + " -> " + toDirection;
    }

    public MoveDirection getToDirection() {
        return toDirection;
    }


}
