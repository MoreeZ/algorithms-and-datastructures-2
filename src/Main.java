import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        HashMap<Integer, Vertex> stops = new HashMap<>();
        HashMap<Integer, LinkedList<Edge>> trips = new HashMap<>();
        loadStopData(stops, "stops.txt");
        generateFloydMatrix();

    }

    static void loadStopData(HashMap<Integer, Vertex> stops, String filename) {
        if (filename == null) {
            return;
        }
        // Read dataLine.
        // sources: File reader setup: https://www.w3schools.com/java/java_files_read.asp
        try {
            // Get file from directory
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);

            // get first line that doesnt store data
            String dataLine = myReader.nextLine();
            // get the graph from data file
            while (myReader.hasNextLine()) {
                try {
                    dataLine = myReader.nextLine();
                    String[] tokens = dataLine.split(",");
                    if (tokens.length < 10) {
                        String[] newTokens = new String[10];
                        for (int i = 0; i < tokens.length; i++) {
                            newTokens[i] = tokens[i];
                        }
                        tokens = newTokens;
                    }

                    int stop_id = Integer.parseInt(tokens[0]);
                    int stop_code;
                    try {
                        stop_code = Integer.parseInt(tokens[1]);
                    } catch (Exception e) {
                        stop_code = -1;
                    }
                    String stop_name = tokens[2];
                    String stop_desc = tokens[3];
                    double stop_lat = Double.parseDouble(tokens[4]);
                    double stop_lon = Double.parseDouble(tokens[5]);
                    String zone_id = tokens[6];
                    String stop_url = tokens[7];
                    int location_type = Integer.parseInt(tokens[8]);
                    String parent_station = tokens[9];
                    Vertex stop = new Vertex(stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, zone_id, stop_url, location_type, parent_station);
                    stops.put(stop_id, stop);

                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();

                    return;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();

        }

    }

    static void generateFloydMatrix(){

    }
}
