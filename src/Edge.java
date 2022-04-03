public class Edge {
    public Vertex from;
    public Vertex to;
    public double distance;
    public int tripId;

    Edge (Vertex from, Vertex to, double distance, int tripId) {
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.tripId = tripId;
    }

}
