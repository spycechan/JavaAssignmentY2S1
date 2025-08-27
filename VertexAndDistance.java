package javaassignment;

public class VertexAndDistance<V> implements Comparable<VertexAndDistance<V>> {
    public final V vertex;
    public final double totalDistance;

    public VertexAndDistance(V vertex, double totalDistance) {
        this.vertex = vertex;
        this.totalDistance = totalDistance;
    }

    @Override
    public int compareTo(VertexAndDistance<V> other) {
        return Double.compare(this.totalDistance, other.totalDistance);
    }
}
