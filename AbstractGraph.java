package javaassignment;

import java.util.*;

public abstract class AbstractGraph<V> implements Graph<V> {
    protected List<V> vertices = new ArrayList<>();
    protected List<List<Edge>> neighbors = new ArrayList<>();

    protected AbstractGraph() {}

    protected AbstractGraph(V[] vertices, List<Edge> edges) {
        for (V vertex : vertices) addVertex(vertex);
        for (Edge edge : edges) addEdge(edge.u, edge.v, edge.weight);
    }

    public int getSize() { return vertices.size(); }
    public List<V> getVertices() { return vertices; }
    public V getVertex(int index) { return vertices.get(index); }
    public int getIndex(V v) { return vertices.indexOf(v); }

    public boolean addVertex(V vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            neighbors.add(new ArrayList<>());
            return true;
        }
        return false;
    }

    public boolean addEdge(int u, int v, double weight) {
        if (u < 0 || u >= getSize() || v < 0 || v >= getSize()) return false;
        neighbors.get(u).add(new Edge(u, v, weight));
        return true;
    }

    public List<Edge> getNeighbors(int index) {
        return neighbors.get(index);
    }

    // --- Convenience mutation helpers for console UI ---
    public boolean removeEdgeByIndices(int u, int v) {
        if (u < 0 || u >= getSize() || v < 0 || v >= getSize()) return false;
        List<Edge> list = neighbors.get(u);
        boolean removed = false;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).v == v) { list.remove(i); removed = true; }
        }
        return removed;
    }

    public boolean removeEdgeByNames(String fromName, String toName) {
        int u = getIndexName(fromName);
        int v = getIndexName(toName);
        return removeEdgeByIndices(u, v);
    }

    public boolean removeVertexByIndex(int idx) {
        if (idx < 0 || idx >= getSize()) return false;

        // Build new vertices and neighbors with adjusted indices
        List<V> newVertices = new ArrayList<>(getSize() - 1);
        List<List<Edge>> newNeighbors = new ArrayList<>(getSize() - 1);

        for (int oldU = 0; oldU < getSize(); oldU++) {
            if (oldU == idx) continue;
            int newU = oldU - (oldU > idx ? 1 : 0);
            newVertices.add(vertices.get(oldU));
            newNeighbors.add(new ArrayList<>());

            for (Edge e : neighbors.get(oldU)) {
                if (e.v == idx) continue; // drop edges to removed vertex
                int newV = e.v - (e.v > idx ? 1 : 0);
                newNeighbors.get(newU).add(new Edge(newU, newV, e.weight));
            }
        }

        // Replace
        this.vertices = newVertices;
        this.neighbors = newNeighbors;
        return true;
    }

    public boolean removeVertexByName(String name) {
        int idx = getIndexName(name);
        return removeVertexByIndex(idx);
    }

    // Helper: faster index by name for City.getName() or toString fallback
    private int getIndexName(String name) {
        for (int i = 0; i < vertices.size(); i++) {
            V v = vertices.get(i);
            String n;
            if (v instanceof City) n = ((City) v).getName();
            else n = String.valueOf(v);
            if (name.equals(n)) return i;
        }
        return -1;
    }
}