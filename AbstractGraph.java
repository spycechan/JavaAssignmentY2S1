/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaassignment;

/**
 *
 * @author event
 */
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
}
