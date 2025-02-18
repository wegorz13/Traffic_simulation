package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Intersection {
    private final Map<MoveDirection,Road> roads = Map.of(MoveDirection.NORTH, new Road(MoveDirection.NORTH), MoveDirection.SOUTH, new Road(MoveDirection.SOUTH), MoveDirection.EAST, new Road(MoveDirection.EAST), MoveDirection.WEST, new Road(MoveDirection.WEST));
    private final static List<List<TrafficFlow>> SAFE_FLOW_COMBINATIONS = List.of(
            List.of(new TrafficFlow(MoveDirection.SOUTH, MoveDirection.NORTH), new TrafficFlow(MoveDirection.NORTH, MoveDirection.SOUTH), new TrafficFlow(MoveDirection.SOUTH, MoveDirection.EAST),new TrafficFlow(MoveDirection.NORTH, MoveDirection.WEST)),
            List.of(new TrafficFlow(MoveDirection.EAST, MoveDirection.WEST), new TrafficFlow(MoveDirection.WEST, MoveDirection.EAST),new TrafficFlow(MoveDirection.EAST, MoveDirection.NORTH),new TrafficFlow(MoveDirection.WEST, MoveDirection.SOUTH)),
            List.of(new TrafficFlow(MoveDirection.NORTH, MoveDirection.EAST), new TrafficFlow(MoveDirection.SOUTH, MoveDirection.WEST),new TrafficFlow(MoveDirection.NORTH, MoveDirection.WEST), new TrafficFlow(MoveDirection.SOUTH, MoveDirection.EAST)),
            List.of(new TrafficFlow(MoveDirection.EAST, MoveDirection.SOUTH), new TrafficFlow(MoveDirection.WEST, MoveDirection.NORTH),new TrafficFlow(MoveDirection.WEST, MoveDirection.SOUTH), new TrafficFlow(MoveDirection.EAST, MoveDirection.NORTH)),
            List.of(new TrafficFlow(MoveDirection.NORTH, MoveDirection.WEST), new TrafficFlow(MoveDirection.NORTH, MoveDirection.SOUTH),new TrafficFlow(MoveDirection.NORTH, MoveDirection.EAST)),
            List.of(new TrafficFlow(MoveDirection.SOUTH, MoveDirection.WEST), new TrafficFlow(MoveDirection.SOUTH, MoveDirection.NORTH),new TrafficFlow(MoveDirection.SOUTH, MoveDirection.EAST)),
            List.of(new TrafficFlow(MoveDirection.WEST, MoveDirection.EAST), new TrafficFlow(MoveDirection.WEST, MoveDirection.SOUTH),new TrafficFlow(MoveDirection.WEST, MoveDirection.EAST)),
            List.of(new TrafficFlow(MoveDirection.EAST, MoveDirection.WEST), new TrafficFlow(MoveDirection.EAST, MoveDirection.SOUTH),new TrafficFlow(MoveDirection.EAST, MoveDirection.NORTH))
    );
    private final static int MIN_OPEN_TIME = 3;
    private final static int MAX_AWAIT_TIME = 6;
    private int activeTime=0;
    private ArrayList<Lane> activeLanes = new ArrayList<>();
    private final ArrayList<Vehicle> movingVehicles = new ArrayList<>();
    private SimulationPresenter presenter;

    public void move(){
        movingVehicles.clear();

        if (activeTime>=MIN_OPEN_TIME || activeLanes.isEmpty()){
            changeActiveLanes();
            activeTime=0;
        }

        for (Lane lane: activeLanes){
            if (lane.willBeOpen() && !lane.isEmpty()){
                movingVehicles.add(lane.getMovingVehicle());
            }
            lane.move();
        }
        updateLaneTime();
        activeTime++;
        this.trafficChanged();
    }

    public void addVehicle(Vehicle vehicle){
        this.roads.get(vehicle.getTrafficFlow().getFromDirection()).addVehicle(vehicle);
        this.trafficChanged();
    }
    
    private ArrayList<Lane> getBusiestLanes(){
        List<TrafficFlow> busiestCombination = new ArrayList<>();
        int maxTraffic=-1;
        
        for (List<TrafficFlow> combination : SAFE_FLOW_COMBINATIONS) {
            int combinationTraffic=0;
            for (TrafficFlow trafficFlow : combination) {
                combinationTraffic += roads.get(trafficFlow.getFromDirection()).getLaneTraffic(trafficFlow.getToDirection());
            }
            if (combinationTraffic > maxTraffic) {
                maxTraffic = combinationTraffic;
                busiestCombination = combination;
            }
        }

        ArrayList<Lane> busiestLanes = new ArrayList<>();
        for (TrafficFlow trafficFlow : busiestCombination) {
            busiestLanes.add(roads.get(trafficFlow.getFromDirection()).getLane(trafficFlow.getToDirection()));
        }

        return busiestLanes;
    }

    private ArrayList<Lane> getMostNeglectedLanes(){
        List<TrafficFlow> mostNeglectedCombination = new ArrayList<>();
        int maxAwaitTime=0;

        for (List<TrafficFlow> combination : SAFE_FLOW_COMBINATIONS) {
            int combinationAwaitTime=0;
            for (TrafficFlow trafficFlow : combination) {
                Lane lane = roads.get(trafficFlow.getFromDirection()).getLane(trafficFlow.getToDirection());
                if (lane.getAwaitTime() >= MAX_AWAIT_TIME && !lane.isEmpty()){
                    combinationAwaitTime+=lane.getAwaitTime();
                }
            }
            if (combinationAwaitTime > maxAwaitTime) {
                maxAwaitTime = combinationAwaitTime;
                mostNeglectedCombination = combination;
            }
        }

        ArrayList<Lane> neglectedLanes = new ArrayList<>();
        for (TrafficFlow trafficFlow : mostNeglectedCombination) {
            neglectedLanes.add(roads.get(trafficFlow.getFromDirection()).getLane(trafficFlow.getToDirection()));
        }

        return neglectedLanes;
    }

    private void updateLaneTime(){
        for (Road road: roads.values()){
            road.updateLaneTime();
        }
    }

    private void changeActiveLanes(){
        ArrayList<Lane> nextLanes;
        nextLanes = getMostNeglectedLanes();
        if (nextLanes.isEmpty()){
            nextLanes = getBusiestLanes();
        }

        if (!nextLanes.equals(activeLanes)){
            for (Lane lane: activeLanes){
                lane.close();
            }
            activeLanes = nextLanes;
        }
    }

    public ArrayList<Vehicle> getMovingVehicles(){
        return this.movingVehicles;
    }

    public void setPresenter(SimulationPresenter presenter){
        this.presenter = presenter;
    }

    private void trafficChanged(){
        if (presenter != null){
            presenter.trafficChanged(this);
        }
    }

    public Map<MoveDirection,Road> getRoads(){
        return this.roads;
    }

}
