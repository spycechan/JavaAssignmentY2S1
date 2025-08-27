package javaassignment;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GraphView<V extends Displayable> extends Pane {

    // Look-and-feel settings
    private static final double CITY_CIRCLE_RADIUS = 26.0;   // size of each city circle
    private static final double ARROW_SIDE_LENGTH  = 12.0;   // length of each arrowhead side
    private static final double LABEL_DISTANCE_FROM_LINE = 8.0; // how far the label sits off the line
    private static final double CITY_NAME_OFFSET_Y = 8.0;    // how far above the circle the city name is drawn

    public GraphView(AbstractGraph<V> graph) {
        // White background for good contrast
        setStyle("-fx-background-color: white;");

        // 1) Draw all edges first (lines, arrowheads, and hour labels)
        for (int sourceIndex = 0; sourceIndex < graph.getSize(); sourceIndex++) {
            V sourceCity = graph.getVertex(sourceIndex);

            for (Edge edge : graph.getNeighbors(sourceIndex)) {
                V destinationCity = graph.getVertex(edge.v);

                // --- A. Read the center coordinates of the two cities ---
                double sourceCenterX = sourceCity.getX();
                double sourceCenterY = sourceCity.getY();
                double destinationCenterX = destinationCity.getX();
                double destinationCenterY = destinationCity.getY();

                // --- B. Compute the direction from source to destination ---
                double deltaX = destinationCenterX - sourceCenterX;
                double deltaY = destinationCenterY - sourceCenterY;

                // Length of the line between the two city centers
                double centerToCenterLength = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                if (centerToCenterLength == 0.0) {
                    // Two cities at the exact same point: skip this edge to avoid divide-by-zero.
                    continue;
                }

                // Unit (length = 1) direction from source to destination
                double directionX = deltaX / centerToCenterLength;
                double directionY = deltaY / centerToCenterLength;

                // --- C. Shorten the line so it touches the circle borders, not the centers ---
                // Move the start forward by the city radius; move the end backward by the city radius.
                double lineStartX = sourceCenterX + directionX * CITY_CIRCLE_RADIUS;
                double lineStartY = sourceCenterY + directionY * CITY_CIRCLE_RADIUS;
                double lineEndX   = destinationCenterX - directionX * CITY_CIRCLE_RADIUS;
                double lineEndY   = destinationCenterY - directionY * CITY_CIRCLE_RADIUS;

                // --- D. Draw the main straight line (flight path) ---
                Line flightLine = new Line(lineStartX, lineStartY, lineEndX, lineEndY);
                flightLine.setStroke(Color.BLACK);
                getChildren().add(flightLine);

                // --- E. Draw the arrowhead at the end of the line ---
                // Angle of the line (in radians)
                double lineAngle = Math.atan2(directionY, directionX);

                // Two side angles for the arrowhead (±25 degrees from the line direction)
                double leftSideAngle  = lineAngle + Math.toRadians(25.0);
                double rightSideAngle = lineAngle - Math.toRadians(25.0);

                // Endpoints of the two arrowhead sides
                double arrowLeftEndX  = lineEndX - ARROW_SIDE_LENGTH * Math.cos(leftSideAngle);
                double arrowLeftEndY  = lineEndY - ARROW_SIDE_LENGTH * Math.sin(leftSideAngle);
                double arrowRightEndX = lineEndX - ARROW_SIDE_LENGTH * Math.cos(rightSideAngle);
                double arrowRightEndY = lineEndY - ARROW_SIDE_LENGTH * Math.sin(rightSideAngle);

                Line arrowLeftSide  = new Line(lineEndX, lineEndY, arrowLeftEndX,  arrowLeftEndY);
                Line arrowRightSide = new Line(lineEndX, lineEndY, arrowRightEndX, arrowRightEndY);
                arrowLeftSide.setStroke(Color.BLACK);
                arrowRightSide.setStroke(Color.BLACK);
                getChildren().addAll(arrowLeftSide, arrowRightSide);

                // --- F. Draw the weight (hours) near the middle of the line ---
                // Midpoint of the shortened line
                double midpointX = (lineStartX + lineEndX) / 2.0;
                double midpointY = (lineStartY + lineEndY) / 2.0;

                // A perpendicular direction to the line (rotate by +90°): (-directionY, directionX)
                double perpendicularX = -directionY;
                double perpendicularY =  directionX;

                // Decide which side to nudge the label so multiple labels don't stack perfectly
                double labelSideFactor;
                if (lineStartX <= lineEndX) {
                    labelSideFactor = 1.0;   // place label on one side
                } else {
                    labelSideFactor = -1.0;  // place label on the other side
                }

                // Final label position
                double labelX = midpointX + labelSideFactor * LABEL_DISTANCE_FROM_LINE * perpendicularX;
                double labelY = midpointY + labelSideFactor * LABEL_DISTANCE_FROM_LINE * perpendicularY;

                Text hoursLabel = new Text(labelX, labelY, formatHours(edge.weight));
                hoursLabel.setFill(Color.BLACK);
                hoursLabel.setStyle("-fx-font-size: 12;");
                getChildren().add(hoursLabel);
            }
        }

        // 2) Draw all nodes (circles) and city names last so they appear on top of the lines
        for (int i = 0; i < graph.getSize(); i++) {
            V city = graph.getVertex(i);

            // City circle
            Circle cityCircle = new Circle(city.getX(), city.getY(), CITY_CIRCLE_RADIUS);
            cityCircle.setFill(Color.BLACK);
            cityCircle.setStroke(Color.BLACK);
            getChildren().add(cityCircle);

            // City name (drawn above the circle; roughly centered by subtracting half the text width estimate)
            String cityName = city.getName();
            double estimatedHalfTextWidth = cityName.length() * 3.5; // simple estimate for centering
            double cityNameX = city.getX() - estimatedHalfTextWidth;
            double cityNameY = city.getY() - CITY_CIRCLE_RADIUS - CITY_NAME_OFFSET_Y;

            Text cityNameText = new Text(cityNameX, cityNameY, cityName);
            cityNameText.setFill(Color.BLACK);
            cityNameText.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
            getChildren().add(cityNameText);
        }
    }

    /**
     * Convert a number of hours into a label like "1h" or "2.5h".
     * Whole numbers are shown without ".0".
     */
    private String formatHours(double hours) {
        double nearestInteger = Math.rint(hours);
        double difference = Math.abs(hours - nearestInteger);

        if (difference < 1e-9) {
            int wholeHours = (int) nearestInteger;
            return wholeHours + "h";
        } else {
            return hours + "h";
        }
    }
}
