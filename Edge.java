package javaassignment;

public class Edge {
    public int u; // Start vertex index
    public int v; // End vertex index
    public double weight; // Edge weight

    public Edge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }
}