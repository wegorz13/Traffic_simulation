import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class App extends Application {
    private Simulation simulation;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        List<String> params = getParameters().getRaw();
        if (params.size() < 2) {
            System.out.println("Input and output file needed");
            System.exit(1);
        }
        String inputFile = params.get(0);
        String outputFile = params.get(1);
        System.out.println(inputFile);
        System.out.println(outputFile);
        this.simulation = new Simulation(inputFile, outputFile);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        configureStage(primaryStage, viewRoot);

        primaryStage.show();

        this.simulation.getIntersection().setPresenter(loader.getController());

        Thread simulationThread = new Thread(simulation);
        simulationThread.start();

    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
