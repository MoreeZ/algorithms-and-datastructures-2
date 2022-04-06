import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        HashMap<Integer, Vertex> stops = new HashMap<>();
        HashMap<Integer, LinkedList<Edge>> trips = new HashMap<>();
        loadStopData(stops, "stops.txt");

        boolean quit = false;
        Scanner input = new Scanner(System.in);
        while (!quit) {
            System.out.println("=========== AVAILABLE OPTIONS ===========");
            System.out.println("(1) Shortest path search");
            System.out.println("(2) Stop information search");
            System.out.println("(3) Trip search with arrival time");
            System.out.println("Type \"quit\" to quit the program.");
            System.out.println("=========================================");
            System.out.print("Select option: ");
            String in = input.nextLine();
            try {
                int option = Integer.parseInt(in);
                switch (option) {
                    case 1:
                        break;
                    case 2:
                        System.out.println("Loading stop search program...");
                        runStopSearch(input, stops);
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Option not available. Please try again.");
                        break;
                }
            } catch (Exception e) {
                if (in.equalsIgnoreCase("quit")) {
                    System.out.println("Quitting program. See you later!");
                    quit = true;
                } else
                    System.out.println("Incorrect input! Please enter a number.");
            }
        }
        input.close();

    }

    static void runStopSearch(Scanner input, HashMap<Integer, Vertex> stops) {
        boolean exit = false;
        while (!exit) {
            System.out.println("============== STOP SEARCH ==============");
            System.out.println("Search for bus stop information with bus stop name.");
            System.out.println("Type \"exit\" to exit.");
            System.out.println("=========================================");
            TernarySearchTree ternarySearch = new TernarySearchTree(stops);
            System.out.print("Bus stop name: ");
            String searchInput = input.nextLine();
            if (searchInput.equalsIgnoreCase("exit")) {
                exit = true;
            } else {
//          example: ANTRIM AVE
                String searchResult = ternarySearch.search(searchInput.toUpperCase());
                Vertex stopData = ternarySearch.getStopData(stops, searchResult);
                if (stopData != null) {
                    System.out.println("=========== STOP INFORMATION ============");
                    System.out.println("ID:\t\t\t\t" + stopData.stop_id);
                    System.out.println("Code:\t\t\t" + stopData.stop_code);
                    System.out.println("Name:\t\t\t" + stopData.stop_name);
                    System.out.println("Description:\t" + stopData.stop_desc);
                    System.out.println("Latitude:\t\t" + stopData.stop_lat);
                    System.out.println("Longitude:\t\t" + stopData.stop_lon);
                    System.out.println("Zone ID:\t\t" + stopData.zone_id);
                    if (stopData.stop_url.trim() == "")
                        System.out.println("URL:\t\t\t\t" + "Not available.");
                    else
                        System.out.println("URL:\t\t\t\t" + stopData.stop_url);
                    System.out.println("Location Type\t" + stopData.location_type);
                    System.out.println("Parent station:\t" + stopData.parent_station);
                } else {
                    System.out.println("There is no bus stop with this name.");
                }
            }
        }
    }

    static void generateFloydMatrix() {

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
}
