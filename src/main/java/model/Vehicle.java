package model;

public class Vehicle {
    private final String id;
    private final TrafficFlow flow;

    public Vehicle(String id, MoveDirection fromDirection, MoveDirection toDirection) {
        this.id = id;
        this.flow = new TrafficFlow(fromDirection, toDirection);
    }

    public String getId() {
        return id;
    }
    public TrafficFlow getTrafficFlow() {
        return this.flow;
    }

    @Override
    public String toString() {
        return "vehicle" + id;
    }
}
