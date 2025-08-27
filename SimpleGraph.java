package javaassignment;

import java.util.List;

// Concrete graph so we don't need anonymous subclasses (which create $1.class)
public class SimpleGraph<V> extends AbstractGraph<V> {
    public SimpleGraph() { super(); }
    public SimpleGraph(V[] vertices, List<Edge> edges) { super(vertices, edges); }
}
