/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaassignment;

/**
 *
 * @author event
 */
import java.util.*;

public class Dijkstra {
    public static <V> Map<V, Double> shortestPath(AbstractGraph<V> graph, V start) {
        int startIndex = graph.getIndex(start);
        Map<V, Double> minDistance = new HashMap<>();
        PriorityQueue<VertexDistance<V>> pq = 
                new PriorityQueue<>(Comparator.comparingDouble(a -> a.distance));
        pq.add(new VertexDistance<>(start, 0));
        minDistance.put(start, 0.0);

        while (!pq.isEmpty()) {
            VertexDistance<V> current = pq.poll();
            int currentIndex = graph.getIndex(current.vertex);
            if (current.distance > minDistance.get(current.vertex)) continue;

            for (Edge edge : graph.getNeighbors(currentIndex)) {
                V neighbor = graph.getVertex(edge.v);
                double newDist = current.distance + edge.weight;
                if (!minDistance.containsKey(neighbor) || newDist < minDistance.get(neighbor)) {
                    minDistance.put(neighbor, newDist);
                    pq.add(new VertexDistance<>(neighbor, newDist));
                }
            }
        }
        return minDistance;
    }

    static class VertexDistance<V> {
        V vertex;
        double distance;
        VertexDistance(V vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }
}
