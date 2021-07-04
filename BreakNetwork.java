import java.io.*;
import java.util.*;

public class BreakNetwork {
    private static Graph mainGraph;

    public static void main(String[] args) throws FileNotFoundException {
        HashMap<Integer, Set<Integer>> mainList = new HashMap<>();
        mainGraph = new Graph(mainList);

        String filename;
        int num_nodes;
        String runType = args[0];

        if (runType.equals("-d")) {     // By degree
            num_nodes = Integer.parseInt(args[1]);
            filename = args[2];
            makeGraphFromFile(filename);
            removeByDegree(mainGraph, num_nodes);
        } else if (runType.equals("-r")) {      // By CI
            int radius = Integer.parseInt(args[1]);
            num_nodes = Integer.parseInt(args[2]);
            filename = args[3];
            makeGraphFromFile(filename);
            removeByCI(mainGraph, radius, num_nodes);
        }
    }

    public static void makeGraphFromFile(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            // Split input file on spaces
            String[] splitArr = (line.split("\\s+"));

            // Add each vertex to graph
            for (String e : splitArr) {
                int parsedAsInt = Integer.parseInt(e);
                if (!mainGraph.containsVertex(parsedAsInt)) {
                    mainGraph.addVertex(parsedAsInt);
                }
            }

            // Create edge between vertices
            int vertA = Integer.parseInt(String.valueOf(splitArr[0]));
            int vertB = Integer.parseInt(String.valueOf(splitArr[1]));
            mainGraph.addEdge(vertA, vertB);
        }
    }

    public static void removeByDegree(Graph g, int num_nodes){
        for (int i = 0; i < num_nodes; i++)
            removeHighestDegree(g);
    }

    public static void removeHighestDegree(Graph g){
        int high = 0;
        int key = 0;

        for (int v : g.getAllVertices()) {
            // Loop through to find the vertex with the most neighbors
            if (g.getNeighbors(v).size() > high) {
                high = g.getNeighbors(v).size();
                key = v;
            }
        }
        System.out.println("Removed " + key + " with degree " + high);
        g.removeVertex(key);
    }

    // Returns set of vertices within a given radius from root
    // When surfaceNodesOnly is true, BFS() will return only those nodes at the *exact* radius from root
    public static Set<Integer> BFS(Graph g, int root, int radius, boolean surfaceNodesOnly) {
        boolean[] visited = new boolean[g.getHighestNode() + 1];
        LinkedList<Integer> queue = new LinkedList<>();

        visited[root] = true;
        queue.add(root);
        queue.add(-1);
        int depth = 0;

        Set<Integer> result = new HashSet<>();

        while (queue.size() != 0 && depth <= radius) {
            int temp = queue.poll();
            if (temp == -1){
                depth++;
                queue.add(-1);
                if (queue.peek() == -1){    // Two consecutive nulls, end of graph
                    break;
                } else
                    continue;
            }

            /*System.out.print("Visited: " + temp);
            System.out.println(" at depth: " + depth);*/

            if (!surfaceNodesOnly){
                result.add(temp);
            } else if (surfaceNodesOnly && depth == radius)
                result.add(temp);

            Iterator<Integer> it = g.getNeighbors(temp).iterator();

            // Enqueues all neighbors of root
            while (it.hasNext()) {
                int n = it.next();

                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
        return result;
    }

    public static int calculateCI(Graph g, int root, int radius) {
        /* Call BFS() on root to find nodes at exactly radius away
            For each of those nodes, find their degree minus one
            Sum those values
            Multiply sum by (degree of root node - 1) */

        Set<Integer> nodesAtRadius = BFS(g, root, radius, true);

        int sum = 0;
        for (int v : nodesAtRadius){
             sum += g.getNeighbors(v).size() - 1;   // Degree - 1
        }

        int result = (g.getNeighbors(root).size() - 1) * sum;

        //System.out.println("CI of node " + root + " is " + result);
        return result;
    }

    public static void removeHighestCI(Graph g, int radius){
        int high = 0;
        int key = 0;

        for (int v : g.getAllVertices()) {
            if (calculateCI(g, v, radius) > high) {
                high = calculateCI(g, v, radius);
                key = v;
            }
        }
        System.out.println("Removing node " + key + " with a CI of " + high);
        // And removes it along with its edges
        g.removeVertex(key);
    }

    public static void removeByCI(Graph g, int radius, int num_nodes){

        /*  FUTURE STEPS
        1) Calculate CI of every node
        2) Remove node with highest CI
        3) Update CI for each node that is <= radius + 1 from removed node
        4) Return to 2)*/

        for (int i=0; i < num_nodes; i++)
            removeHighestCI(g, radius);
    }
}