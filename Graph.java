import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Graph {
    // Adjacency list graph representation holds nodes as keys and their neighbors as values
    final private HashMap<Integer, Set<Integer>> connectionsList;
    private int nodeCount;

    public Graph(HashMap<Integer, Set<Integer>> clist) {
        this.connectionsList = clist;
    }

    public Set<Integer> getNeighbors(int v){
        return this.connectionsList.get(v);
    }

    public Set<Integer> getAllVertices() {
        return this.connectionsList.keySet();
    }

    public boolean containsVertex(Integer v){
        return this.connectionsList.containsKey(v);
    }

    public int getHighestNode(){
        return Collections.max(getAllVertices());
    }

    // Adds vertex with empty list of neighbors
    public void addVertex(Integer v) {
        if (containsVertex(v)) {
            throw new IllegalArgumentException("Vertex already exists.");
        }

        this.connectionsList.put(v, new HashSet<>());
        nodeCount++;
    }

    public void removeVertex(Integer v) {
        if (!containsVertex(v)) {
            throw new IllegalArgumentException("No such vertex to remove");
        }

        this.connectionsList.remove(v);

        // Removes target vertex from all vertices neighboring it
        for (Integer u : this.getAllVertices()) {
            this.connectionsList.get(u).remove(v);
        }

        nodeCount--;
    }

    public void addEdge(Integer v, Integer u) {
        if (!this.containsVertex(v) || !this.containsVertex(u)) {
            throw new IllegalArgumentException("No such vertices to make an edge between");
        }

        // Add each vertex to the other's list of neighbors
        this.connectionsList.get(v).add(u);
        this.connectionsList.get(u).add(v);
    }
}
