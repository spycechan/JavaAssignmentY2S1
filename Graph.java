package javaassignment;

public interface Graph<V> {
    int getSize();
    java.util.List<V> getVertices();
    V getVertex(int index);
    int getIndex(V v);
    boolean addVertex(V vertex);
    boolean addEdge(int u, int v, double weight);
}