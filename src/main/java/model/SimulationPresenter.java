package model;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SimulationPresenter {
    @FXML
    private GridPane mapGrid;
    private Intersection intersection;
    private final int cellSize = 27;
    private final int mapSize = 800;
    private final int vehicleLimit = 10;

    public void trafficChanged(Intersection intersection){
        Platform.runLater(()->{
            this.intersection = intersection;
            drawMap();
        });
    }

    private void drawMap(){
        clearGrid();
        createConstraints();
        drawIntersection();
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void createConstraints(){
        for (int i=0;i<(mapSize/cellSize);i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        }
    }

    private void drawIntersection(){
        mapGrid.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        for (Road road : intersection.getRoads().values()){
            for (Lane lane : road.getLanes().values()){
                drawLane(lane);
            }
        }
    }

    private void drawLane(Lane lane){
        MoveDirection fromDirection = lane.getTrafficFlow().fromDirection();
        MoveDirection toDirection = lane.getTrafficFlow().toDirection();
        int xStart = 0;
        int yStart = 0;
        int xChange = 0;
        int yChange = 0;
        String sign = "";
        int numberOfVehicles = lane.getTrafficSize();
        LightColor currentColor = lane.getCurrentLightColor();

        //i know this is very bad bud I had no time to do something different
        if (fromDirection == MoveDirection.NORTH){
            xStart = vehicleLimit + 2;
            yStart = vehicleLimit + 1;
            xChange = 0;
            yChange = -1;
            sign = "<";
            if (toDirection == MoveDirection.SOUTH){
                xStart+=1;
                sign = "v";
            }
            else if (toDirection == MoveDirection.EAST){
                xStart+=2;
                sign = ">";
            }
        }
        else if (fromDirection == MoveDirection.SOUTH){
            xStart = vehicleLimit + 4;
            yStart = vehicleLimit + 7;
            xChange = 0;
            yChange = 1;
            sign = "<";
            if (toDirection == MoveDirection.NORTH){
                xStart+=1;
                sign = "^";
            }
            else if (toDirection == MoveDirection.EAST){
                xStart+=2;
                sign = ">";
            }
        }
        else if (fromDirection == MoveDirection.WEST){
            xStart = vehicleLimit + 1;
            yStart = vehicleLimit + 4;
            xChange = -1;
            yChange = 0;
            sign = "^";
            if (toDirection == MoveDirection.EAST){
                yStart+=1;
                sign = ">";
            }
            else if (toDirection == MoveDirection.SOUTH){
                yStart+=2;
                sign = "v";
            }
        }
        else if (fromDirection == MoveDirection.EAST){
            xStart = vehicleLimit + 7;
            yStart = vehicleLimit + 2;
            xChange = 1;
            yChange = 0;
            sign = "^";
            if (toDirection == MoveDirection.WEST){
                yStart+=1;
                sign = "<";
            }
            else if (toDirection == MoveDirection.SOUTH){
                yStart+=2;
                sign = "v";
            }
        }

        Circle circle = new Circle(cellSize/2.5);
        circle.setFill(currentColor.toPaintColor());
        mapGrid.add(circle, xStart, yStart);
        GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
        xStart+=xChange;
        yStart+=yChange;
        Label label = new Label(sign);
        mapGrid.add(label, xStart, yStart);
        GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);

        int counter = Math.min(numberOfVehicles, vehicleLimit);
        for (int i=0; i<counter; i++){
            xStart+=xChange;
            yStart+=yChange;

            Rectangle rectangle = new Rectangle(0.7*cellSize, 0.7*cellSize);
            rectangle.setFill(Color.LIGHTBLUE);
            mapGrid.add(rectangle, xStart, yStart);
            GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
        }
    }
}
