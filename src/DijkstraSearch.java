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

    DijkstraSearch(String filename) {
        this.numOfEdges = 0;
        this.numOfVertices = 0;
        this.Vertices = new HashMap<>();

        try {
            File myFile = new File(filename);
            Scanner input = new Scanner(myFile);
            this.numOfVertices = input.nextInt();
            this.numOfEdges = input.nextInt();
            distanceArr = new double[numOfVertices];
            for (int i = 0; i < this.numOfVertices; i++) {
                distanceArr[i] = Double.POSITIVE_INFINITY;
            }
            previousArr = new int[numOfVertices];
            for (int i = 0; i < this.numOfVertices; i++) {
                previousArr[i] = -1;
            }


            while (input.hasNextLine()) {
                try {
                    int from = Integer.parseInt(input.next());
                    int to = Integer.parseInt(input.next());
                    double length = Double.parseDouble(input.next());
                    ArrayList<Edge> list = new ArrayList<>();
                    if (Vertices.containsKey(from)) {
                        list = (ArrayList<Edge>) Vertices.get(from);
                    }
                    Edge workingEdge = new Edge(from, to, length);
                    list.add(workingEdge);
                    Vertices.put(from, list);
                } catch (Exception e) {
                    this.numOfEdges = 0;
                    this.numOfVertices = 0;
                    this.Vertices = new HashMap<Integer, List<Edge>>();
                    return;
                }
            }
            input.close();
        } catch (FileNotFoundException e) {
            this.numOfVertices = 0;
            this.numOfEdges = 0;
            e.printStackTrace();
        }
    }

    public double getDistance(int startingPoint, int endingPoint) {
        boolean[] visitedArr = new boolean[numOfVertices];
        for (int i = 0; i < numOfVertices; i++) {
            visitedArr[i] = false;
        }

        distanceArr = new double[numOfVertices];
        for (int i = 0; i < this.numOfVertices; i++) {
            distanceArr[i] = Double.POSITIVE_INFINITY;
        }

        previousArr = new int[numOfVertices];
        for (int i = 0; i < this.numOfVertices; i++) {
            previousArr[i] = -1;
        }
        distanceArr[startingPoint] = 0;

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


        return distanceArr[endingPoint];
    }

    public class Edge {
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