import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LaneTest {
    @Test
    void moveIsCorrect() {
        //given
        Lane lane = new Lane(new TrafficFlow(MoveDirection.SOUTH, MoveDirection.NORTH));
        lane.addVehicle(new Vehicle("vehicle1", MoveDirection.SOUTH, MoveDirection.NORTH));

        //when then

        lane.move();
        assertEquals(lane.getCurrentLightColor(), LightColor.YELLOW);
        assertFalse(lane.isEmpty());
        lane.move();
        assertEquals(lane.getCurrentLightColor(), LightColor.GREEN);
        assertTrue(lane.isEmpty());
    }

    @Test
    void closeIsCorrect() {
        //given
        Lane lane = new Lane(new TrafficFlow(MoveDirection.SOUTH, MoveDirection.WEST));

        //when
        lane.close();

        //then
        assertEquals(lane.getCurrentLightColor(), LightColor.RED);
    }
}
