import model.Intersection;
import model.MoveDirection;
import model.Vehicle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Simulation implements Runnable {
    private final Intersection intersection = new Intersection();
    private final ArrayList<ArrayList<String>> stepStatuses = new ArrayList<>();
    private final String inputFilePath;
    private final String outputFilePath;

    public Simulation(String inputFilePath,String outputFilePath ) {
        this.outputFilePath = outputFilePath;
        this.inputFilePath = inputFilePath;
    }

    public void run() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));

            JSONObject jsonObject = new JSONObject(content);
            JSONArray commands = jsonObject.getJSONArray("commands");

            for (int i = 0; i < commands.length(); i++) {
                JSONObject command = commands.getJSONObject(i);
                String type = command.getString("type");

                try {
                if (type.equals("addVehicle")) {
                    String vehicleId = command.getString("vehicleId");
                    String startRoad = command.getString("startRoad");
                    String endRoad = command.getString("endRoad");

                    Vehicle vehicle = new Vehicle(vehicleId, MoveDirection.fromString(startRoad), MoveDirection.fromString(endRoad));
                    intersection.addVehicle(vehicle);

                } else if (type.equals("step")) {
                    intersection.move();
                    updateStatuses(intersection.getMovingVehicles());
                }
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            saveStatusesToFile();
        } catch (Exception e) {
            System.err.println("Error: The file " + inputFilePath + " was not found.");
            System.exit(1);
        }
    }

    private void updateStatuses(ArrayList<Vehicle> vehicles) {
        ArrayList<String> stepStatus = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            stepStatus.add(vehicle.getId());
        }
        stepStatuses.add(stepStatus);
    }

    private void saveStatusesToFile(){
        System.out.println(stepStatuses);
        JSONObject root = new JSONObject();

        JSONArray stepStatusesArray = new JSONArray();

        for (ArrayList<String> vehicles : stepStatuses) {
            JSONObject stepStatus = new JSONObject();
            JSONArray leftVehicles = new JSONArray();
            for (String vehicle : vehicles) {
                leftVehicles.put(vehicle);
            }
            stepStatus.put("leftVehicles", leftVehicles);
            stepStatusesArray.put(stepStatus);
        }

        root.put("stepStatuses", stepStatusesArray);

        try (FileWriter file = new FileWriter(outputFilePath)) {
            file.write(root.toString());

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Intersection getIntersection() {
        return intersection;
    }
}
