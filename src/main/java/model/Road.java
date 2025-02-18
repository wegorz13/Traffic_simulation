package model;

import java.util.HashMap;
import java.util.Map;

public class Road {
    private final Map<MoveDirection,Lane> lanes = new HashMap<MoveDirection,Lane>();
    private final MoveDirection fromDirection;

    public Road(MoveDirection direction) {
        this.fromDirection = direction;
        lanes.put(direction.next().next(), new Lane(new TrafficFlow(this.fromDirection, direction.next().next())));
        lanes.put(direction.next(), new Lane(new TrafficFlow(this.fromDirection, direction.next())));
        lanes.put(direction.previous(), new Lane(new TrafficFlow(this.fromDirection, direction.previous())));
    }

    public int getLaneTraffic(MoveDirection direction) {
        return lanes.get(direction).getTrafficSize();
    }

    public Lane getLane(MoveDirection direction) {
        return lanes.get(direction);
    }

    public void updateLaneTime(){
        for (Lane lane : lanes.values()) {
            lane.updateTime();
        }
    }

    public void addVehicle(Vehicle vehicle) {
        lanes.get(vehicle.getTrafficFlow().getToDirection()).addVehicle(vehicle);
    }

    public Map<MoveDirection,Lane> getLanes(){
        return lanes;
    }
}
