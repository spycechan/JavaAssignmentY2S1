package javaassignment;

import java.util.*;

/**
 * Dijkstra's Algorithm:
 * Finds the minimum total weight (shortest distance) from a start vertex to all
 * other reachable vertices in a weighted graph with non-negative edge weights.
 *
 * Result:
 *   The returned map contains only the vertices that are reachable from 'start'.
 *   Each value is the best (smallest) total distance found from 'start' to that vertex.
 *
 * Requirements on the graph API (assumed from your project):
 *   - graph.getSize() -> number of vertices
 *   - graph.getVertex(index) -> vertex object at that index
 *   - graph.getIndex(vertex) -> index of the given vertex (or -1 if not present)
 *   - graph.getNeighbors(index) -> Iterable<Edge> of outgoing edges from vertex at 'index'
 *   - Edge has: int v (destination vertex index), double weight (edge cost; must be >= 0)
 */
public class Dijkstra {

    public static <V> Map<V, Double> shortestPath(AbstractGraph<V> graph, V startVertex) {
        // Find index of the start vertex inside the graph.
        int startIndex = graph.getIndex(startVertex);
        if (startIndex < 0) {
            // If the start vertex is not in the graph, there are no paths to compute.
            return Collections.emptyMap();
        }

        // This map stores, for each vertex, the best (smallest) distance found so far from the start.
        Map<V, Double> distanceFromStart = new HashMap<>();

    // Priority queue ordered by totalDistance via VertexAndDistance.compareTo
    PriorityQueue<VertexAndDistance<V>> priorityQueue = new PriorityQueue<>();

        // Initialize with the start vertex: distance to itself is 0.
        distanceFromStart.put(startVertex, 0.0);
        priorityQueue.offer(new VertexAndDistance<>(startVertex, 0.0));

        // Main loop: keep expanding the closest not-yet-processed vertex.
        while (!priorityQueue.isEmpty()) {
            // Get the vertex that currently has the smallest known distance.
            VertexAndDistance<V> current = priorityQueue.poll();

            // If this entry is "stale" (we have already found a better distance), skip it.
            Double bestKnownForCurrent = distanceFromStart.get(current.vertex);
            if (bestKnownForCurrent != null && current.totalDistance > bestKnownForCurrent) {
                continue;
            }

            // Get the graph index to iterate over its outgoing edges.
            int currentIndex = graph.getIndex(current.vertex);

            // Explore all neighbors reachable by one edge from the current vertex.
            for (Edge edge : graph.getNeighbors(currentIndex)) {
                // Convert neighbor index back to the actual vertex object.
                V neighborVertex = graph.getVertex(edge.v);

                // Candidate distance if we go current -> neighbor using this edge.
                double candidateDistance = current.totalDistance + edge.weight;

                // If neighbor has no known distance yet, or this path is better, update it.
                Double bestKnownForNeighbor = distanceFromStart.get(neighborVertex);
                if (bestKnownForNeighbor == null || candidateDistance < bestKnownForNeighbor) {
                    distanceFromStart.put(neighborVertex, candidateDistance);
                    // Push the neighbor with its new distance into the priority queue.
                    priorityQueue.offer(new VertexAndDistance<>(neighborVertex, candidateDistance));
                }
            }
        }

        return distanceFromStart;
    }

    /**
     * Beginner-friendly variant that fills the given maps with results:
     *  - distancesOut: best total cost from start to each reachable vertex
     *  - parentOut:    parent of each vertex on the cheapest path tree (start has parent null)
     */
    public static <V> void shortestPathFrom(
            AbstractGraph<V> graph,
            V startVertex,
            Map<V, Double> distancesOut,
            Map<V, V> parentOut) {

        distancesOut.clear();
        parentOut.clear();

        int startIndex = graph.getIndex(startVertex);
        if (startIndex < 0) return; // start not in graph

        // Priority queue ordered by total distance
        PriorityQueue<VertexAndDistance<V>> pq = new PriorityQueue<>();
        distancesOut.put(startVertex, 0.0);
        parentOut.put(startVertex, null);
        pq.offer(new VertexAndDistance<>(startVertex, 0.0));

        while (!pq.isEmpty()) {
            VertexAndDistance<V> cur = pq.poll();
            Double best = distancesOut.get(cur.vertex);
            if (best != null && cur.totalDistance > best) continue; // stale

            int uIndex = graph.getIndex(cur.vertex);
            for (Edge e : graph.getNeighbors(uIndex)) {
                V vtx = graph.getVertex(e.v);
                double cand = cur.totalDistance + e.weight;
                Double known = distancesOut.get(vtx);
                if (known == null || cand < known) {
                    distancesOut.put(vtx, cand);
                    parentOut.put(vtx, cur.vertex);
                    pq.offer(new VertexAndDistance<>(vtx, cand));
                }
            }
        }
    }

    /**
     * Reconstruct the cheapest path using the parent map filled by shortestPathFrom.
     * Returns an empty list if target is unreachable.
     */
    public static <V> List<V> reconstructPath(AbstractGraph<V> graph, V start, V target, Map<V, V> parent) {
        if (!parent.containsKey(start) || !parent.containsKey(target)) return Collections.emptyList();
        LinkedList<V> path = new LinkedList<>();
        V cur = target;
        while (cur != null) {
            path.addFirst(cur);
            if (cur.equals(start)) break;
            cur = parent.get(cur);
            if (cur == null) return Collections.emptyList();
        }
        return path;
    }
}
