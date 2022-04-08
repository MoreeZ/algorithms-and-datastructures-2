//trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type,shape_dist_traveled
public class Trip {
    public int trip_id;
    public String arrival_time;
    public String departure_time;
    public int stop_id;
    public int stop_sequence;
    public String stop_headsign;
    public int pickup_type;
    public int drop_off_type;
    public double shape_dist_traveled;


    Trip(int trip_id, String arrival_time, String departure_time, int stop_id, int stop_sequence, String stop_headsign, int pickup_type, int drop_off_type, double shape_dist_traveled) {
        this.trip_id = trip_id;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.stop_id = stop_id;
        this.stop_sequence = stop_sequence;
        this.stop_headsign = stop_headsign;
        this.pickup_type = pickup_type;
        this.drop_off_type = drop_off_type;
        this.shape_dist_traveled = shape_dist_traveled;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "trip_id=" + trip_id +
                ", arrival_time='" + arrival_time + '\'' +
                ", departure_time='" + departure_time + '\'' +
                ", stop_id=" + stop_id +
                ", stop_sequence=" + stop_sequence +
                '}';
    }
}
