import java.util.HashMap;

public class Stop {
    public int stop_id;
    public int stop_code;
    public String stop_name;
    public String stop_desc;
    public double stop_lat;
    public double stop_lon;
    public String zone_id;
    public String stop_url;
    public int location_type;
    public String parent_station;

    Stop(int stop_id,
         int stop_code,
         String stop_name,
         String stop_desc,
         double stop_lat,
         double stop_lon,
         String zone_id,
         String stop_url,
         int location_type,
         String parent_station) {
        this.stop_id = stop_id;
        this.stop_code = stop_code;
        this.stop_name = stop_name;
        this.stop_desc = stop_desc;
        this.stop_lat = stop_lat;
        this.stop_lon = stop_lon;
        this.zone_id =zone_id;
        this.stop_url = stop_url;
        this.location_type = location_type;
        this.parent_station = parent_station;
    }
    Stop() {}
}
//Hashmap<Integer, Stop> myHashmap = new HashMap<>();
//Stop stop = new Stop(a b c d e f g); // makes new stop with all information a b c d e f g
//myHashmap.put(stop.stop_id, stop);
//
//// because it contains all the stop information