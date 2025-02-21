package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private int activeTime = 0;
    private ArrayList<Lane> activeLanes = new ArrayList<>();

    private SimulationPresenter presenter;

    public ArrayList<Vehicle> move(){
        ArrayList<Vehicle> movingVehicles = new ArrayList<>();

        if (activeTime>=MIN_OPEN_TIME || activeLanes.isEmpty()){
            changeActiveLanes();
            activeTime = 0;
        }

        for (Lane lane: activeLanes){
            Optional<Vehicle> movingVehicle = lane.move();
            movingVehicle.ifPresent(movingVehicles::add);
        }
        updateLaneTime();
        activeTime++;
        this.trafficChanged();

        return movingVehicles;
    }

    public void addVehicle(Vehicle vehicle){
        this.roads.get(vehicle.getTrafficFlow().fromDirection()).addVehicle(vehicle);
        this.trafficChanged();
    }

    private ArrayList<Lane> getNextLanes(){
        List<TrafficFlow> mostNeglectedCombination = new ArrayList<>();
        int maxAwaitTime = 0;
        List<TrafficFlow> busiestCombination = new ArrayList<>();
        int maxTraffic = -1;

        for (List<TrafficFlow> combination : SAFE_FLOW_COMBINATIONS) {
            int combinationAwaitTime = 0;
            int combinationTraffic = 0;

            for (TrafficFlow trafficFlow : combination) {
                Lane lane = roads.get(trafficFlow.fromDirection()).getLane(trafficFlow.toDirection());
                if (lane.getAwaitTime() >= MAX_AWAIT_TIME && !lane.isEmpty()){
                    combinationAwaitTime+=lane.getAwaitTime();
                }
                combinationTraffic+=lane.getTrafficSize();
            }
            if (combinationAwaitTime > maxAwaitTime) {
                maxAwaitTime = combinationAwaitTime;
                mostNeglectedCombination = combination;
            }
            if (combinationTraffic > maxTraffic) {
                maxTraffic = combinationTraffic;
                busiestCombination = combination;
            }
        }
        List<TrafficFlow> nextCombination;

        if (!mostNeglectedCombination.isEmpty()){
             nextCombination = mostNeglectedCombination;
        }
        else nextCombination = busiestCombination;

        ArrayList<Lane> nextLanes = new ArrayList<>();
        for (TrafficFlow trafficFlow : nextCombination) {
            nextLanes.add(roads.get(trafficFlow.fromDirection()).getLane(trafficFlow.toDirection()));
        }

        return nextLanes;
    }

    private void updateLaneTime(){
        for (Road road: roads.values()){
            road.updateLaneTime();
        }
    }

    private void changeActiveLanes(){
        ArrayList<Lane> nextLanes = getNextLanes();

        if (!nextLanes.equals(activeLanes)){
            for (Lane lane : activeLanes){
                lane.close();
            }
            activeLanes = nextLanes;
        }
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
