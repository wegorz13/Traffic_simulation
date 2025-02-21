package model;

import java.util.LinkedList;
import java.util.Optional;

public class Lane {
    private final TrafficFlow trafficFlow;
    private final LinkedList<Vehicle> vehicles = new LinkedList<>();
    private LightColor currentLightColor = LightColor.RED;
    private int awaitTime = 0;

    public Lane(TrafficFlow trafficFlow) {
        this.trafficFlow = trafficFlow;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public Optional<Vehicle> move(){
        currentLightColor = currentLightColor.next();
        if (this.currentLightColor == LightColor.GREEN && !vehicles.isEmpty()) {
            return Optional.of(vehicles.removeFirst());
        }

        return Optional.empty();
    }

    public void close(){
        currentLightColor = currentLightColor.previous();
        currentLightColor = currentLightColor.previous();
    }

    public void updateTime(){
        if (currentLightColor == LightColor.RED){
            awaitTime++;
        }
    }

    public boolean isEmpty(){
        return vehicles.isEmpty();
    }

    public int getAwaitTime() {
        return awaitTime;
    }

    public int getTrafficSize(){
        return this.vehicles.size();
    }

    public TrafficFlow getTrafficFlow() {
        return trafficFlow;
    }

    public LightColor getCurrentLightColor(){
        return currentLightColor;
    }
}
