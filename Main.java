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

public class Main {
    public static void main(String[] args) {
        City[] cities = {
            new City("Malaysia", 400, 300),
            new City("Singapore", 500, 350),
            new City("Thailand", 350, 200),
            new City("Indonesia", 450, 400),
            new City("UAE", 100, 400),
            new City("Japan", 800, 80),
            new City("Australia", 900, 500),
            new City("Vietnam", 300, 150),
            new City("Philippines", 600, 200),
            new City("China", 700, 100),
            new City("South Korea", 750, 50),
            new City("India", 200, 350)
        };

        List<Edge> flights = Arrays.asList(
            new Edge(0, 1, 1), new Edge(0, 2, 2), 
            new Edge(0, 3, 2.5), new Edge(0, 4, 7),
            new Edge(1, 0, 1), new Edge(1, 5, 7), 
            new Edge(1, 6, 8),
            new Edge(2, 7, 1.5), new Edge(2, 0, 2), 
            new Edge(2, 9, 4.5),
            new Edge(3, 0, 2.5), new Edge(3, 8, 3.5), 
            new Edge(3, 6, 6),
            new Edge(7, 9, 3), new Edge(7, 5, 5),
            new Edge(8, 5, 4.5), new Edge(8, 10, 4.5),
            new Edge(9, 5, 3), new Edge(9, 10, 2.5), 
            new Edge(9, 11, 6),
            new Edge(5, 10, 2), new Edge(5, 6, 8),
            new Edge(10, 5, 2), new Edge(10, 9, 2.5),
            new Edge(11, 4, 3.5), new Edge(11, 0, 5),
            new Edge(6, 5, 8), new Edge(6, 1, 8),
            new Edge(4, 11, 3.5), new Edge(4, 0, 7)
        );

        AbstractGraph<City> graph = new AbstractGraph<City>(cities, flights) {};
        Map<City, Double> shortest = Dijkstra.shortestPath(graph, cities[0]);
        for (City city : shortest.keySet()) {
            System.out.println("Shortest duration from Malaysia to " + city.getName() + " is " + shortest.get(city) + " hours.");
        }
    }
}
