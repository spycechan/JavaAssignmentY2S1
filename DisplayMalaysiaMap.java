package javaassignment;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.*;

public class DisplayMalaysiaMap extends Application {
    // Allow launching the JavaFX window from the console app using an existing graph
    private static volatile boolean platformStarted = false;
    private static AbstractGraph<City> externalGraph = null;

    // Show a window for the provided graph without creating another Application class
    public static void showGraph(AbstractGraph<City> graph) {
        externalGraph = graph;
        ensurePlatform();
        Platform.runLater(() -> {
            try {
                new DisplayMalaysiaMap().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private static synchronized void ensurePlatform() {
        if (!platformStarted) {
            try {
                Platform.startup(() -> {});
            } catch (IllegalStateException already) {
                // Platform was already started (e.g., via Application.launch)
            }
            // Keep JavaFX running after the last window is closed so we can open it again
            Platform.runLater(() -> Platform.setImplicitExit(false));
            platformStarted = true;
        } else {
            // If already started, make sure implicit exit stays disabled
            Platform.runLater(() -> Platform.setImplicitExit(false));
        }
    }

    @Override
    public void start(Stage primaryStage) {
        AbstractGraph<City> graph;
        if (externalGraph != null && externalGraph.getSize() > 0) {
            graph = externalGraph; // use the provided graph from the console UI
        } else {
            City[] cities = {
                // Even wider spacing between tight clusters (SEA and NE Asia)
                new City("Malaysia", 440, 340),       // center-left
                new City("Singapore", 780, 480),      // right and down from Malaysia
                new City("Thailand", 380, 220),       // above Malaysia
                new City("Indonesia", 700, 600),      // bottom-right of Singapore
                new City("UAE", 70, 540),             // far left-bottom
                new City("Japan", 1160, 220),         // far right
                new City("Australia", 1120, 620),     // far right-bottom
                new City("Vietnam", 320, 140),        // top-left cluster
                new City("Philippines", 900, 300),    // right of China
                new City("China", 760, 140),          // between Korea and Philippines
                new City("South Korea", 980, 120),    // top-right
                new City("India", 240, 320)           // left of Malaysia
            };

            List<Edge> flights = Arrays.asList(
                new Edge(0, 1, 1), new Edge(0, 2, 2), new Edge(0, 3, 2.5), new Edge(0, 4, 7),
                new Edge(1, 0, 1), new Edge(1, 5, 7), new Edge(1, 6, 8),
                new Edge(2, 7, 1.5), new Edge(2, 0, 2), new Edge(2, 9, 4.5),
                new Edge(3, 0, 2.5), new Edge(3, 8, 3.5), new Edge(3, 6, 6),
                new Edge(7, 9, 3), new Edge(7, 5, 5),
                new Edge(8, 5, 4.5), new Edge(8, 10, 4.5),
                new Edge(9, 5, 3), new Edge(9, 10, 2.5), new Edge(9, 11, 6),
                new Edge(5, 10, 2), new Edge(5, 6, 8),
                new Edge(10, 5, 2), new Edge(10, 9, 2.5),
                new Edge(11, 4, 3.5), new Edge(11, 0, 5),
                new Edge(6, 5, 8), new Edge(6, 1, 8),
                new Edge(4, 11, 3.5), new Edge(4, 0, 7)
            );

            graph = new AbstractGraph<City>(cities, flights) {};
        }
        GraphView<City> view = new GraphView<>(graph);

    Scene scene = new Scene(view, 1300, 700);
        primaryStage.setTitle("Malaysia Airline Flight Network");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
