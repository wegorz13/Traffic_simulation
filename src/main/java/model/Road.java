package model;

import java.util.HashMap;
import java.util.Map;

public class Road {
    private final Map<MoveDirection,Lane> lanes = new HashMap<MoveDirection,Lane>();
    private final MoveDirection fromDirection;

    public Road(MoveDirection direction){
        this.fromDirection = direction;
        put(direction.next().next());
        put(direction.next());
        put(direction.previous());
    }

    private void put(MoveDirection direction){
        lanes.put(direction, new Lane(new TrafficFlow(this.fromDirection, direction)));
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
        lanes.get(vehicle.getTrafficFlow().toDirection()).addVehicle(vehicle);
    }

    public Map<MoveDirection,Lane> getLanes(){
        return lanes;
    }
}
