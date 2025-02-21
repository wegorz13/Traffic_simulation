import model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class IntersectionTest {
    @Test
    void correctRoadsAreSelected(){
        //given
        Intersection intersection = new Intersection();

        //when
        for (int i=0; i<15; i++){
            intersection.addVehicle(new Vehicle("vehicle1", MoveDirection.SOUTH, MoveDirection.NORTH));
            intersection.addVehicle(new Vehicle("vehicle2", MoveDirection.NORTH, MoveDirection.SOUTH));
        }
        intersection.addVehicle(new Vehicle("vehicle3", MoveDirection.EAST, MoveDirection.WEST));
        intersection.addVehicle(new Vehicle("vehicle4", MoveDirection.WEST, MoveDirection.EAST));

        intersection.move();
        intersection.move();

        //then
        Map<MoveDirection, Road> roads = intersection.getRoads();
        Lane northForwardLane = roads.get(MoveDirection.NORTH).getLane(MoveDirection.SOUTH);
        Lane southForwardLane = roads.get(MoveDirection.SOUTH).getLane(MoveDirection.NORTH);

        assertEquals(northForwardLane.getCurrentLightColor(),LightColor.GREEN);
        assertEquals(southForwardLane.getCurrentLightColor(),LightColor.GREEN);

        //when
        for (int i=0; i<6; i++){
            intersection.move();
        }

        //then
        Lane eastForwardLane = roads.get(MoveDirection.EAST).getLane(MoveDirection.WEST);
        Lane westForwardLane = roads.get(MoveDirection.WEST).getLane(MoveDirection.EAST);

        assertEquals(LightColor.GREEN, eastForwardLane.getCurrentLightColor());
        assertEquals(LightColor.GREEN, westForwardLane.getCurrentLightColor());
    }

    @Test
    void movingVehiclesAreCorrect(){
        //given
        Intersection intersection = new Intersection();

        //when
        intersection.addVehicle(new Vehicle("vehicle1", MoveDirection.SOUTH, MoveDirection.NORTH));
        intersection.addVehicle(new Vehicle("vehicle2", MoveDirection.NORTH, MoveDirection.SOUTH));

        intersection.move(); //yellow light


        //then
        List<Vehicle> vehicles = intersection.move();

        assertEquals(2, vehicles.size());
        assertNotEquals(vehicles.get(0), vehicles.get(1));
    }
}
