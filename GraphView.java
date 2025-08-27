/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaassignment;

/**
 *
 * @author event
 */
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GraphView<V extends Displayable> extends Pane {
	private static final double NODE_RADIUS = 26.0;
	private static final double ARROW_LENGTH = 12.0;

	public GraphView(AbstractGraph<V> graph) {
		// White background so black text/arrows are readable
		setStyle("-fx-background-color: white;");

		// Draw edges first (so nodes sit on top)
		java.util.Set<String> labeledPairs = new java.util.HashSet<>();
		for (int u = 0; u < graph.getSize(); u++) {
			for (Edge e : graph.getNeighbors(u)) {
				V src = graph.getVertex(u);
				V dst = graph.getVertex(e.v);

				// Draw the directed edge line + arrow
				drawDirectedEdge(src, dst);

				// Build undirected pair key
				String key = (u < e.v) ? (u + "-" + e.v) : (e.v + "-" + u);
				if (!labeledPairs.contains(key)) {
					// Find reverse weight if exists
					Double reverseWeight = null;
					for (Edge re : graph.getNeighbors(e.v)) {
						if (re.v == u) { reverseWeight = re.weight; break; }
					}

					String labelText;
					if (reverseWeight != null) {
						if (Math.abs(e.weight - reverseWeight) < 1e-9) {
							labelText = trimHours(e.weight) + "h";
						} else {
							labelText = trimHours(e.weight) + "h/" + trimHours(reverseWeight) + "h";
						}
					} else {
						labelText = trimHours(e.weight) + "h";
					}

					drawEdgeLabel(src, dst, labelText);
					labeledPairs.add(key);
				}
			}
		}

		// Draw nodes and labels last
		for (int i = 0; i < graph.getSize(); i++) {
			V city = graph.getVertex(i);
			Circle circle = new Circle(city.getX(), city.getY(), NODE_RADIUS);
			circle.setFill(Color.BLACK);
			circle.setStroke(Color.BLACK);

			// City name above node, centered approximately
			Text name = new Text(city.getX() - city.getName().length() * 3.5, city.getY() - NODE_RADIUS - 8, city.getName());
			name.setFill(Color.BLACK);

			getChildren().addAll(circle, name);
		}
	}

	private void drawDirectedEdge(V src, V dst) {
		double sx = src.getX();
		double sy = src.getY();
		double ex = dst.getX();
		double ey = dst.getY();

		// Direction vector
		double dx = ex - sx;
		double dy = ey - sy;
		double len = Math.hypot(dx, dy);
		if (len == 0) return; // avoid degenerate
		double nx = dx / len;
		double ny = dy / len;

		// Trim so the line touches node borders, not centers
		double startX = sx + nx * NODE_RADIUS;
		double startY = sy + ny * NODE_RADIUS;
		double endX = ex - nx * NODE_RADIUS;
		double endY = ey - ny * NODE_RADIUS;

		// Main straight line
		Line edgeLine = new Line(startX, startY, endX, endY);
		edgeLine.setStroke(Color.BLACK);
		getChildren().add(edgeLine);

		// Arrowhead at end of straight line
		double tx = nx; // unit tangent along the line
		double ty = ny;

		double ax1x = endX - ARROW_LENGTH * (tx * Math.cos(Math.toRadians(25)) - ty * Math.sin(Math.toRadians(25)));
		double ax1y = endY - ARROW_LENGTH * (tx * Math.sin(Math.toRadians(25)) + ty * Math.cos(Math.toRadians(25)));
		double ax2x = endX - ARROW_LENGTH * (tx * Math.cos(Math.toRadians(-25)) - ty * Math.sin(Math.toRadians(-25)));
		double ax2y = endY - ARROW_LENGTH * (tx * Math.sin(Math.toRadians(-25)) + ty * Math.cos(Math.toRadians(-25)));

		Line arrow1 = new Line(endX, endY, ax1x, ax1y);
		Line arrow2 = new Line(endX, endY, ax2x, ax2y);
		arrow1.setStroke(Color.BLACK);
		arrow2.setStroke(Color.BLACK);
		getChildren().addAll(arrow1, arrow2);

	}

	private void drawEdgeLabel(V src, V dst, String text) {
		double sx = src.getX();
		double sy = src.getY();
		double ex = dst.getX();
		double ey = dst.getY();

		double dx = ex - sx;
		double dy = ey - sy;
		double len = Math.hypot(dx, dy);
		if (len == 0) return;
		double nx = dx / len;
		double ny = dy / len;

		// Trim to node borders
		double startX = sx + nx * NODE_RADIUS;
		double startY = sy + ny * NODE_RADIUS;
		double endX = ex - nx * NODE_RADIUS;
		double endY = ey - ny * NODE_RADIUS;

		// Midpoint with small perpendicular nudge for readability
		double mx = (startX + endX) / 2.0;
		double my = (startY + endY) / 2.0;
		double px = -ny;
		double py = nx;
		Text weightText = new Text(mx + 8 * px, my + 8 * py, text);
		weightText.setFill(Color.BLACK);
		getChildren().add(weightText);
	}

	private String trimHours(double h) {
		// Render integer hours without .0
		if (Math.abs(h - Math.rint(h)) < 1e-9) {
			return Integer.toString((int)Math.rint(h));
		}
		return Double.toString(h);
	}
}


