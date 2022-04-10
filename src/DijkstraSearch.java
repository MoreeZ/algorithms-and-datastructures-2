import java.util.*;
import java.io.*;

public class DijkstraSearch {
    /**
     * @param filename: A filename containing the details of the city road network
     * @param sA,       sB, sC: speeds for 3 contestants
     */
    HashMap<Integer, List<Edge>> Vertices;
    int numOfVertices;
    int numOfEdges;
    double[] distanceArr;
    int[] previousArr;

    DijkstraSearch(ArrayList<Edge> edgeList, HashMap<Integer, Stop> stops) {
        this.Vertices = new HashMap<>();
        this.numOfVertices = stops.size();
        this.numOfEdges = edgeList.size();

        try {
            // setup distanceArr
            distanceArr = new double[numOfVertices];
            for (int i = 0; i < this.numOfVertices; i++) {
                distanceArr[i] = Double.POSITIVE_INFINITY;
            }
            // setup previous vertex array
            previousArr = new int[numOfVertices];
            for (int i = 0; i < this.numOfVertices; i++) {
                previousArr[i] = -1;
            }
            // load edge data into hashmap
            for (int i = 0; i < edgeList.size(); i++) {
                try {
                    int from = edgeList.get(i).from;
                    int to = edgeList.get(i).to;
                    double weight = edgeList.get(i).weight;
                    List<Edge> list = new ArrayList<>();
                    if (Vertices.containsKey(from)) {
                        list = Vertices.get(from);
                    }
                    Edge workingEdge = new Edge(from, to, weight);
                    list.add(workingEdge);
                    Vertices.put(from, list);
                } catch (Exception e) {
                    this.numOfEdges = 0;
                    this.numOfVertices = 0;
                    this.Vertices = new HashMap<>();
                    return;
                }
            }
        } catch (Exception e) {
            this.numOfVertices = 0;
            this.numOfEdges = 0;
            this.Vertices = new HashMap<>();
            e.printStackTrace();
        }
    }

    public double getDistance(int startingPoint, int endingPoint) {
        boolean[] visitedArr = new boolean[numOfVertices];

        distanceArr = new double[numOfVertices];
        for (int i = 0; i < this.numOfVertices; i++) {
            distanceArr[i] = Double.POSITIVE_INFINITY;
        }

        previousArr = new int[numOfVertices];
        for (int i = 0; i < this.numOfVertices; i++) {
            previousArr[i] = -1;
        }
        distanceArr[startingPoint] = 0;

        try {
            for (int i = 0; i < numOfVertices - 1; i++) {
                double min = Double.POSITIVE_INFINITY;
                int u = 0;
                for (int k = 0; k < numOfVertices; k++) {
                    if (distanceArr[k] <= min && visitedArr[k] == false) {
                        min = distanceArr[k];
                        u = k;
                    }
                }
                List<Edge> workingEdges = Vertices.get(u);
                visitedArr[u] = true;

                if (workingEdges != null) {
                    for (int j = 0; j < workingEdges.size(); j++) {
                        Edge test = workingEdges.get(j);
                        if (!visitedArr[workingEdges.get(j).to]) {
                            int v = workingEdges.get(j).to;
                            double combinedDist = distanceArr[u] + workingEdges.get(j).weight;
                            if (combinedDist < distanceArr[v]) {
                                distanceArr[v] = combinedDist;
                                previousArr[v] = workingEdges.get(j).from;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error calculating shortest path :(");
            e.printStackTrace(System.out);
        }

        return distanceArr[endingPoint];
    }

    public static class Edge {
        double weight;
        int from;
        int to;

        Edge(int from, int to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}