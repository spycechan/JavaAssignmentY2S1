/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaassignment;

/**
 *
 * @author event
 */
public interface Graph<V> {
    int getSize();
    java.util.List<V> getVertices();
    V getVertex(int index);
    int getIndex(V v);
    boolean addVertex(V vertex);
    boolean addEdge(int u, int v, double weight);
}
