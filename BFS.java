package javaassignment;

import java.util.*;

/**
 * Breadth-First Search (BFS) utilities for a generic graph.
 *
 * - traverse(...) returns the order of vertices visited starting from a given start vertex.
 * - hopDistances(...) returns, for every vertex, the minimum number of edges (hops) from the start.
 *
 * Assumptions:
 *   AbstractGraph<V> provides:
 *     - int getSize()
 *     - V getVertex(int index)
 *     - int getIndex(V vertex)
 *     - Iterable<Edge> getNeighbors(int index)   // neighbors of vertex at 'index'
 *   Edge provides:
 *     - int v        // neighbor vertex index
 *     - double weight (ignored by BFS, which treats all edges as 1 hop)
 */
public class BFS<V> {

    /** A readable alias for "unreachable" distance. */
    private static final int UNREACHABLE = Integer.MAX_VALUE;

    /**
     * Perform a breadth-first traversal and return the visit order,
     * starting from the given 'start' vertex.
     */
    public List<V> traverse(AbstractGraph<V> graph, V start) {
        int numberOfVertices = graph.getSize();

        // visitedFlags[i] is true once vertex i has been discovered (enqueued)
        boolean[] visitedFlags = new boolean[numberOfVertices];

        // Queue for BFS frontier (vertices to explore next)
        Queue<Integer> vertexQueue = new ArrayDeque<>();

        // The final ordered list of visited vertices
        List<V> visitOrderList = new ArrayList<>();

        // Find the array index of the start vertex inside the graph
        int startIndex = graph.getIndex(start);
        if (startIndex < 0) {
            // Start vertex not found in the graph: return empty order
            return visitOrderList;
        }

        // Initialize BFS with the start vertex
        visitedFlags[startIndex] = true;      // mark as discovered
        vertexQueue.offer(startIndex);        // enqueue for exploration

        // Standard BFS loop
        while (!vertexQueue.isEmpty()) {
            // Take the next vertex from the queue
            int currentIndex = vertexQueue.poll();

            // Record it in the visit order (this is when we "visit" the vertex)
            visitOrderList.add(graph.getVertex(currentIndex));

            // Explore all outward edges (neighbors) from this vertex
            for (Edge edge : graph.getNeighbors(currentIndex)) {
                int neighborIndex = edge.v;

                // If we have not discovered this neighbor yet, discover it now
                if (!visitedFlags[neighborIndex]) {
                    visitedFlags[neighborIndex] = true;  // mark discovered so we do not enqueue twice
                    vertexQueue.offer(neighborIndex);    // enqueue to visit later
                }
            }
        }

        return visitOrderList;
    }

    /**
     * Compute the minimum number of edges (hops) from 'start' to every vertex.
     * If a vertex cannot be reached from 'start', its distance is UNREACHABLE (Integer.MAX_VALUE).
     */
    public Map<V, Integer> hopDistances(AbstractGraph<V> graph, V start) {
        int numberOfVertices = graph.getSize();

        // distances[i] will store the hop-count from start to vertex i
        int[] distances = new int[numberOfVertices];
        Arrays.fill(distances, UNREACHABLE);

        // Locate the start vertex index
        int startIndex = graph.getIndex(start);
        if (startIndex < 0) {
            // Start vertex not found: return an empty map
            return Collections.emptyMap();
        }

        // BFS frontier
        Queue<Integer> vertexQueue = new ArrayDeque<>();

        // Distance to the start vertex is 0 hops
        distances[startIndex] = 0;
        vertexQueue.offer(startIndex);

        // Standard BFS over edges, updating hop counts
        while (!vertexQueue.isEmpty()) {
            int currentIndex = vertexQueue.poll();

            for (Edge edge : graph.getNeighbors(currentIndex)) {
                int neighborIndex = edge.v;

                // If neighbor has not been reached yet, set its distance and enqueue it
                if (distances[neighborIndex] == UNREACHABLE) {
                    distances[neighborIndex] = distances[currentIndex] + 1; // one more hop
                    vertexQueue.offer(neighborIndex);
                }
            }
        }

        // Build a result map from vertex object -> hop distance.
        // LinkedHashMap preserves the graph's vertex order.
        Map<V, Integer> vertexToDistance = new LinkedHashMap<>();
        for (int vertexIndex = 0; vertexIndex < numberOfVertices; vertexIndex++) {
            V vertexObject = graph.getVertex(vertexIndex);
            int hopCount = distances[vertexIndex];
            vertexToDistance.put(vertexObject, hopCount);
        }

        return vertexToDistance;
    }

    /**
     * Build the BFS tree (parent pointers) from 'start'.
     * Returns a map from each discovered vertex to its parent vertex in the tree.
     * The start vertex is included in the map and has parent null.
     */
    public Map<V, V> bfsTree(AbstractGraph<V> graph, V start) {
        int n = graph.getSize();

        boolean[] visited = new boolean[n];
        int[] parentIndex = new int[n];
        Arrays.fill(parentIndex, -1);

        int startIndex = graph.getIndex(start);
        if (startIndex < 0) {
            return Collections.emptyMap();
        }

        Queue<Integer> q = new ArrayDeque<>();
        visited[startIndex] = true;
        q.offer(startIndex);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (Edge e : graph.getNeighbors(u)) {
                int v = e.v;
                if (!visited[v]) {
                    visited[v] = true;
                    parentIndex[v] = u;
                    q.offer(v);
                }
            }
        }

        Map<V, V> parentMap = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            if (visited[i]) {
                V child = graph.getVertex(i);
                V parent = parentIndex[i] == -1 ? null : graph.getVertex(parentIndex[i]);
                parentMap.put(child, parent);
            }
        }
        return parentMap;
    }

    /**
     * Compute a shortest-hop path from 'start' to 'target'. If unreachable, returns an empty list.
     */
    public List<V> shortestHopPath(AbstractGraph<V> graph, V start, V target) {
        int n = graph.getSize();
        int startIndex = graph.getIndex(start);
        int targetIndex = graph.getIndex(target);
        if (startIndex < 0 || targetIndex < 0) return Collections.emptyList();

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        Queue<Integer> q = new ArrayDeque<>();
        visited[startIndex] = true;
        q.offer(startIndex);

        while (!q.isEmpty()) {
            int u = q.poll();
            if (u == targetIndex) break; // found
            for (Edge e : graph.getNeighbors(u)) {
                int v = e.v;
                if (!visited[v]) {
                    visited[v] = true;
                    parent[v] = u;
                    q.offer(v);
                }
            }
        }

        if (!visited[targetIndex]) return Collections.emptyList();

        // Reconstruct from target back to start
        LinkedList<V> path = new LinkedList<>();
        for (int cur = targetIndex; cur != -1; cur = parent[cur]) {
            path.addFirst(graph.getVertex(cur));
            if (cur == startIndex) break;
        }
        return path;
    }
}
