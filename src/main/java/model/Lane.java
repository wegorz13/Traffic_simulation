package model;

import java.util.LinkedList;

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

    public void move(){
        currentLightColor = currentLightColor.next();
        if (this.currentLightColor == LightColor.GREEN && !vehicles.isEmpty()) {
            vehicles.removeFirst();
        }
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

    public boolean willBeOpen(){
        return currentLightColor == LightColor.YELLOW || currentLightColor == LightColor.GREEN;
    }

    public boolean isEmpty(){
        return vehicles.isEmpty();
    }

    public Vehicle getMovingVehicle(){
        return vehicles.getFirst();
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
