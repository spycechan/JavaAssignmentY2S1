package javaassignment;

import java.util.*;

public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
    // Start with an empty graph (no anonymous class -> no $ in class files)
    AbstractGraph<City> graph = new SimpleGraph<>();

    // Preload a sample dataset so you don't have to create it every time
    seedSample(graph);

        while (true) {
            printMainMenu(graph);
            String choice = in.nextLine().trim();
            switch (choice) {
                case "1":
                    createGraphMenu(graph);
                    break;
                case "2":
                    searchAirport(graph);
                    break;
                case "3":
                    viewNetwork(graph);
                    break;
                case "4":
                    runBFS(graph);
                    break;
                case "5":
                    runDijkstra(graph);
                    break;
                case "6":
                    showGraphWindow(graph);
                    break;
                case "0":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid selection. Please choose 0 to 6.\n");
            }
        }
    }

    // --- Menus ---
    private static void printMainMenu(AbstractGraph<City> graph) {
        clear();
        int cities = graph.getSize();
        int flights = countFlights(graph);
        System.out.println("+-----------------------------------------------------------+");
        System.out.println("| Malaysia Airlines Flight Network (Console)               |");
        System.out.println("+-----------------------------------------------------------+");
        System.out.printf ("| Cities: %-4d | Flights: %-4d                               |%n", cities, flights);
        System.out.println("+-----------------------------------------------------------+\n");

        System.out.println("  [1] Create Graph           - add/remove cities and flights");
        System.out.println("  [2] Search for an Airport  - quick find by name");
        System.out.println("  [3] View Flight Network    - text listing of routes");
        System.out.println("  [4] Shortest Path          - traversal + hop distances");
        System.out.println("  [5] Shortest Time          - shortest durations");
        System.out.println("  [6] Show Graph             - open visual map window");
        System.out.println("  [0] Exit\n");
        System.out.print("Enter your choice (0-6): ");
    }

    // Simple helpers to keep things beginner-friendly
    private static void clear() {
        // Cheap console clear: just add some blank lines
        for (int i = 0; i < 3; i++) System.out.println();
    }

    private static void pause() {
        System.out.print("\nPress ENTER to return to the menu...");
        in.nextLine();
    }

    private static int countFlights(AbstractGraph<City> graph) {
        int total = 0;
        for (int i = 0; i < graph.getSize(); i++) total += graph.getNeighbors(i).size();
        return total;
    }

    private static void createGraphMenu(AbstractGraph<City> graph) {
        while (true) {
            System.out.println();
            System.out.println("Create Graph: Enter \"1\" to \"4\" for updating the graph.");
            System.out.println("------------------------------------------------------------");
            System.out.println("  1. Add a vertex");
            System.out.println("  2. Remove a vertex");
            System.out.println("  3. Add an edge");
            System.out.println("  4. Remove an edge");
            System.out.println("  5. Return to the main menu");
            System.out.print("\nSelection: ");

            String sel = in.nextLine().trim();
            switch (sel) {
                case "1": addVertex(graph); break;
                case "2": removeVertex(graph); break;
                case "3": addEdge(graph); break;
                case "4": removeEdge(graph); break;
                case "5": return;
                default: System.out.println("Invalid selection.\n");
            }
        }
    }

    // --- Actions ---
    private static void addVertex(AbstractGraph<City> graph) {
        System.out.println();
        System.out.println("#(1) Add a vertex");
        System.out.println("------------------------------------------------------------");
        System.out.print("    Enter the name of the city: ");
        String name = in.nextLine().trim();
        if (name.isEmpty()) { System.out.println("    Name cannot be empty.\n"); return; }
        if (findCityIndex(graph, name) != -1) { System.out.println("    City already exists.\n"); return; }

        // Auto-place new city: simple grid based on count
        int i = graph.getSize();
        double x = 150 + (i % 8) * 120;
        double y = 120 + (i / 8) * 100;
        boolean ok = graph.addVertex(new City(name, x, y));
        System.out.println(ok ? "    Added." : "    Failed to add.");
    }

    private static void removeVertex(AbstractGraph<City> graph) {
        System.out.println();
        System.out.println("#(2) Remove a vertex");
        System.out.println("------------------------------------------------------------");
        System.out.print("    Enter the name of the city: ");
        String name = in.nextLine().trim();
        boolean ok = graph.removeVertexByName(name);
        System.out.println(ok ? "    Removed." : "    Not found.");
    }

    private static void addEdge(AbstractGraph<City> graph) {
        System.out.println();
        System.out.println("#(3) Add an edge");
        System.out.println("------------------------------------------------------------");
        System.out.print("    From city: ");
        String from = in.nextLine().trim();
        System.out.print("    To city: ");
        String to = in.nextLine().trim();
        int u = findCityIndex(graph, from);
        int v = findCityIndex(graph, to);
        if (u == -1 || v == -1) { System.out.println("    Unknown city name(s).\n"); return; }
        System.out.print("    Duration (hours, e.g., 2.5): ");
        String wStr = in.nextLine().trim();
        double w;
        try { w = Double.parseDouble(wStr); } catch (Exception ex) { System.out.println("    Invalid number.\n"); return; }
        boolean ok = graph.addEdge(u, v, w);
        System.out.println(ok ? "    Edge added." : "    Failed to add edge.");
    }

    private static void removeEdge(AbstractGraph<City> graph) {
        System.out.println();
        System.out.println("#(4) Remove an edge");
        System.out.println("------------------------------------------------------------");
        System.out.print("    From city: ");
        String from = in.nextLine().trim();
        System.out.print("    To city: ");
        String to = in.nextLine().trim();
        boolean ok = graph.removeEdgeByNames(from, to);
        System.out.println(ok ? "    Edge removed." : "    Edge not found.");
    }

    private static void searchAirport(AbstractGraph<City> graph) {
        System.out.println();
        System.out.println("Search for an Airport");
        System.out.println("------------------------------------------------------------");
        System.out.print("    Enter name (full or part): ");
        String q = in.nextLine().trim().toLowerCase();
        boolean any = false;
        for (int i = 0; i < graph.getSize(); i++) {
            City c = graph.getVertex(i);
            if (c.getName().toLowerCase().contains(q)) {
                System.out.println("    [" + i + "] " + c.getName() + "  (" + (int)c.getX() + "," + (int)c.getY() + ")");
                any = true;
            }
        }
        if (!any) System.out.println("    No matches.");
    pause();
    }

    private static void viewNetwork(AbstractGraph<City> graph) {
        System.out.println();
        System.out.println("MAS Flight Network (text view)");
        System.out.println("------------------------------------------------------------");
        for (int i = 0; i < graph.getSize(); i++) {
            City u = graph.getVertex(i);
            System.out.print(" - " + u.getName() + " -> ");
            List<Edge> nbrs = graph.getNeighbors(i);
            if (nbrs.isEmpty()) { System.out.println("(no outgoing flights)"); continue; }
            for (int k = 0; k < nbrs.size(); k++) {
                Edge e = nbrs.get(k);
                City v = graph.getVertex(e.v);
                System.out.print(v.getName() + "(" + e.weight + "h)");
                if (k + 1 < nbrs.size()) System.out.print(", ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void showGraphWindow(AbstractGraph<City> graph) {
        if (graph.getSize() == 0) {
            System.out.println("\nGraph is empty. Add cities first.\n");
            pause();
            return;
        }
        System.out.println("\nOpening JavaFX graph window... (it may appear behind the console)\n");
        try {
            DisplayMalaysiaMap.showGraph(graph);
        } catch (Throwable t) {
            System.out.println("Unable to open JavaFX window. Ensure JavaFX modules are on the classpath/module-path when running.");
        }
        pause();
    }

    private static void runBFS(AbstractGraph<City> graph) {
        if (graph.getSize() == 0) { System.out.println("\nGraph is empty. Add cities first.\n"); return; }
        System.out.print("\nBFS start city (default: Malaysia if present): ");
        String name = in.nextLine().trim();
        City start = pickCityByNameOrDefault(graph, name, "Malaysia");
        if (start == null) { System.out.println("City not found.\n"); return; }

        BFS<City> bfs = new BFS<>();
        List<City> order = bfs.traverse(graph, start);
        System.out.println("\nBFS order from " + start.getName() + ":");
        for (City c : order) System.out.println(" - " + c.getName());

        Map<City, Integer> hops = bfs.hopDistances(graph, start);
        System.out.println("\nHop distances from " + start.getName() + ":");
        for (City c : graph.getVertices()) {
            int d = hops.getOrDefault(c, Integer.MAX_VALUE);
            System.out.println(c.getName() + ": " + (d == Integer.MAX_VALUE ? "unreachable" : d + " hops"));
        }

        // Show solution path to every reachable city
        System.out.println("\nShortest-hop path from " + start.getName() + " to each city:");
        for (City c : graph.getVertices()) {
            List<City> path = new BFS<City>().shortestHopPath(graph, start, c);
            if (path.isEmpty()) {
                System.out.println(" - " + start.getName() + " -> " + c.getName() + ": unreachable");
            } else {
                System.out.print(" - " + start.getName() + " -> " + c.getName() + ": ");
                printPath(path);
            }
        }

        // Specific path query
        System.out.print("\nFind path to a specific target? (enter name or leave blank): ");
        String tName = in.nextLine().trim();
        if (!tName.isEmpty()) {
            City target = pickCityByNameOrDefault(graph, tName, null);
            if (target == null) {
                System.out.println("  Target not found.");
            } else {
                List<City> path = bfs.shortestHopPath(graph, start, target);
                if (path.isEmpty()) System.out.println("  Unreachable.");
                else {
                    System.out.print("  Path: ");
                    printPath(path);
                }
            }
        }

        System.out.println();
        pause();
    }

    private static void runDijkstra(AbstractGraph<City> graph) {
        if (graph.getSize() == 0) { System.out.println("\nGraph is empty. Add cities first.\n"); return; }
        System.out.print("\nDijkstra start city (default: Malaysia if present): ");
        String name = in.nextLine().trim();
        City start = pickCityByNameOrDefault(graph, name, "Malaysia");
        if (start == null) { System.out.println("City not found.\n"); return; }

        // Compute cheapest distances and parents for reconstructing paths
        Map<City, Double> dist = new LinkedHashMap<>();
        Map<City, City> parent = new LinkedHashMap<>();
        Dijkstra.shortestPathFrom(graph, start, dist, parent);

        System.out.println("\nCheapest total durations from " + start.getName() + ":");
        for (City c : graph.getVertices()) {
            Double t = dist.get(c);
            System.out.println(c.getName() + ": " + (t == null ? "unreachable" : t + " h"));
        }

        // Show cheapest path to every city
        System.out.println("\nCheapest path from " + start.getName() + " to each city:");
        for (City c : graph.getVertices()) {
            Double t = dist.get(c);
            if (t == null) {
                System.out.println(" - " + start.getName() + " -> " + c.getName() + ": unreachable");
            } else {
                List<City> path = Dijkstra.reconstructPath(graph, start, c, parent);
                System.out.print(" - " + start.getName() + " -> " + c.getName() + " (" + t + " h): ");
                printPath(path);
            }
        }

        // Specific target
        System.out.print("\nCheck a specific target? (leave blank to skip): ");
        String targetName = in.nextLine().trim();
        if (!targetName.isEmpty()) {
            City target = pickCityByNameOrDefault(graph, targetName, null);
            if (target == null) System.out.println("  Target not found.");
            else {
                Double t = dist.get(target);
                if (t == null) System.out.println("  Unreachable.");
                else {
                    List<City> path = Dijkstra.reconstructPath(graph, start, target, parent);
                    System.out.print("  Path (" + t + " h): ");
                    printPath(path);
                }
            }
        }
        System.out.println();
        pause();
    }

    // --- Helpers ---
    private static void seedSample(AbstractGraph<City> graph) {
        if (graph.getSize() > 0) return; // already has data

        City[] cities = {
            new City("Malaysia", 440, 340),
            new City("Singapore", 780, 480),
            new City("Thailand", 380, 220),
            new City("Indonesia", 700, 600),
            new City("UAE", 70, 540),
            new City("Japan", 1160, 220),
            new City("Australia", 1120, 620),
            new City("Vietnam", 320, 140),
            new City("Philippines", 900, 300),
            new City("China", 760, 170),
            new City("South Korea", 980, 120),
            new City("India", 240, 320)
        };
        for (City c : cities) graph.addVertex(c);

        List<Edge> flights = Arrays.asList(
            new Edge(0, 1, 1), new Edge(0, 2, 2), new Edge(0, 3, 2.5), new Edge(0, 4, 7),
            new Edge(1, 0, 1), new Edge(1, 5, 7), new Edge(1, 6, 8),
            new Edge(2, 7, 1.5), new Edge(2, 0, 2), new Edge(2, 9, 4.5),
            new Edge(3, 0, 2.5), new Edge(3, 8, 3.5), new Edge(3, 6, 6),
            new Edge(7, 9, 3), new Edge(7, 5, 5),
            new Edge(8, 5, 4.5), new Edge(8, 10, 4.5),
            new Edge(9, 5, 3), new Edge(9, 10, 2.5), new Edge(9, 11, 6),
            new Edge(5, 10, 2), new Edge(5, 6, 8),
            new Edge(10, 5, 2), new Edge(10, 9, 2.5),
            new Edge(11, 4, 3.5), new Edge(11, 0, 5),
            new Edge(6, 5, 8), new Edge(6, 1, 8),
            new Edge(4, 11, 3.5), new Edge(4, 0, 7)
        );
        for (Edge e : flights) graph.addEdge(e.u, e.v, e.weight);

        System.out.println("(Loaded sample data: 12 cities, " + flights.size() + " flights)\n");
    }

    private static int findCityIndex(AbstractGraph<City> graph, String name) {
        for (int i = 0; i < graph.getSize(); i++) {
            if (graph.getVertex(i).getName().equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    private static City pickCityByNameOrDefault(AbstractGraph<City> graph, String name, String defaultName) {
        City picked = null;
        if (name != null && !name.isEmpty()) {
            for (int i = 0; i < graph.getSize(); i++) {
                City c = graph.getVertex(i);
                if (c.getName().equalsIgnoreCase(name)) { picked = c; break; }
            }
        }
        if (picked == null && defaultName != null) {
            for (int i = 0; i < graph.getSize(); i++) {
                City c = graph.getVertex(i);
                if (c.getName().equalsIgnoreCase(defaultName)) { picked = c; break; }
            }
        }
        return picked;
    }

    private static void printPath(List<City> path) {
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i).getName());
            if (i + 1 < path.size()) System.out.print(" -> ");
        }
        System.out.println();
    }
}