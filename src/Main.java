import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        HashMap<Integer, Stop> stops = new HashMap<>();
        loadStopData(stops, "stops.txt");

        boolean quit = false;
        Scanner input = new Scanner(System.in);
        while (!quit) {
            System.out.println("=========== AVAILABLE OPTIONS ===========");
            System.out.println("(1) Shortest path search");
            System.out.println("(2) Bus stop information search");
            System.out.println("(3) Trip search with arrival time");
            System.out.println("Type \"quit\" to quit the program.");
            System.out.println("=========================================");
            System.out.print("Select option: ");
            String in = input.nextLine();
            try {
                int option = Integer.parseInt(in);
                switch (option) {
                    case 1:
                        System.out.println("Loading shortest path between bus stops program...");
                        runShortestPath(input);
                        break;
                    case 2:
                        System.out.println("Loading stop search program...");
                        runStopSearch(input, stops);
                        break;
                    case 3:
                        System.out.println("Loading trip finder with arrival time program...");
                        runSearchTrips(input, stops);
                        break;
                    default:
                        System.out.println("Option not available. Please try again.");
                        break;
                }
            } catch (Exception e) {
                if (in.equalsIgnoreCase("quit")) {
                    System.out.print("Quitting program. See you later!");
                    quit = true;
                } else
                    System.out.println("Incorrect input! Please enter a number.");
            }
        }
        input.close();

    }

    static void runShortestPath(Scanner input) {
        boolean exit = false;
        while (!exit) {
            System.out.println("=========== FIND SHORTEST PATH ==========");
            System.out.println("Enter the departure bus stop and the\n" +
                    "destination bus stop to find the shortest\n" +
                    "path between the stops.");
            System.out.println("Type \"exit\" to exit.");
            System.out.println("=========================================");
            System.out.print("Bus stop ID: ");
            String searchInput = input.nextLine();
            if (searchInput.equalsIgnoreCase("exit")) {
                exit = true;
            } else {
//          example: ANTRIM AVE
                System.out.println("UNIMPLEMENTED SHORTEST PATH FUNCTIONALITY");
            }
        }
    }

    static void runStopSearch(Scanner input, HashMap<Integer, Stop> stops) {
        TernarySearchTree ternarySearch = new TernarySearchTree(stops);
        boolean exit = false;
        while (!exit) {
            System.out.println("============== STOP SEARCH ==============");
            System.out.println("Search for bus stop information with bus stop name.");
            System.out.println("Type \"exit\" to exit.");
            System.out.println("=========================================");
            System.out.print("Bus stop name: ");
            String searchInput = input.nextLine();
            if (searchInput.equalsIgnoreCase("exit")) {
                exit = true;
            } else {
//          example: ANTRIM AVE
                String searchResult = ternarySearch.search(searchInput.toUpperCase());
                Stop stopData = ternarySearch.getStopData(stops, searchResult);
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

    static void runSearchTrips(Scanner input, HashMap<Integer, Stop> stops) {
        HashMap<Integer, LinkedList<Trip>> trips = new HashMap<>();

        boolean exit = false;
        while (!exit) {
            System.out.println("=========== FIND SHORTEST PATH ==========");
            System.out.println(
                    "Enter the an arrival time and we will show\n" +
                            "you a list of trips you can take to arrive\n" +
                            "at that time");
            System.out.println("Type \"exit\" to exit.");
            System.out.println("=========================================");
            System.out.print("Arrival time (hh:mm:ss): ");
            String arrivalTimeSearchIn = input.nextLine();
            if (arrivalTimeSearchIn.equalsIgnoreCase("exit")) {
                exit = true;
            } else {
                Pattern r = Pattern.compile("^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$");
                Matcher m = r.matcher(arrivalTimeSearchIn);
                if (m.matches()) {
                    System.out.println("Loading paths containing the arrival time \"" + arrivalTimeSearchIn + "\"...");
                    loadRelevantPaths(trips, stops, "stop_times.txt", arrivalTimeSearchIn);
                    System.out.println("=========================================");
                    System.out.println("Found " + trips.size() + " trips.");
                    for (Map.Entry<Integer, LinkedList<Trip>> set : trips.entrySet()) {
                        Stop arrivalStop = new Stop();
                        for (int i = 0; i < set.getValue().size(); i++) {
                            if (timeStringToSeconds(set.getValue().get(i).arrival_time) == timeStringToSeconds(arrivalTimeSearchIn)) {
                                arrivalStop = stops.get(set.getValue().get(i).stop_id);
                            }
                        }
                        System.out.println("Trip ID: " + set.getKey() +
                                ".\nLeaving from stop (ID): " + stops.get(set.getValue().getFirst().stop_id).stop_name +
                                ".\nEnding at stop: " + stops.get(set.getValue().getLast().stop_id).stop_name +
                                "\nArriving at " + arrivalTimeSearchIn + " at stop: " + arrivalStop.stop_name);
                        System.out.println("=========================================");
                    }
                } else
                    System.out.println("Invalid input format!");
            }
        }
    }

    static void generateFloydMatrix() {

    }

    static void loadStopData(HashMap<Integer, Stop> stops, String filename) {
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
                    Stop stop = new Stop(stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, zone_id, stop_url, location_type, parent_station);
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

    static void loadRelevantPaths(HashMap<Integer, LinkedList<Trip>> trips, HashMap<Integer, Stop> stops, String filename, String inputStr) {
        if (filename == null) {
            return;
        }
        try {
            // Get file from directory
            File myFile = new File(filename);
            Scanner myReader = new Scanner(myFile);
            String dataLine = myReader.nextLine(); // get first line that doesnt store data

            int inputArrivalTimeInSeconds = timeStringToSeconds(inputStr);
            int previousID = 0;
            LinkedList<Trip> tempList = new LinkedList<>();
            boolean matchesArrivalTime = false;

            while (myReader.hasNextLine()) {
                try {
                    // Load a line of text data
                    dataLine = myReader.nextLine();
                    // split the line onto an array
                    String[] tokens = dataLine.split(",");
                    if (tokens.length < 9) {
                        String[] newTokens = new String[9];
                        for (int i = 0; i < tokens.length; i++) {
                            newTokens[i] = tokens[i];
                        }
                        tokens = newTokens;
                    }
                    // place all data values into variables
                    int trip_id = Integer.parseInt(tokens[0]);
                    String arrival_time = tokens[1];
                    String departure_time = tokens[2]; // wrong
                    int stop_id = Integer.parseInt(tokens[3]);
                    int stop_sequence = Integer.parseInt(tokens[4]);
                    String stop_headsign = tokens[5];
                    int pickup_type = Integer.parseInt(tokens[6]);
                    int drop_off_type = Integer.parseInt(tokens[7]);
                    double shape_dist_traveled;
                    if (tokens[8] == null)
                        shape_dist_traveled = 0.0;
                    else
                        shape_dist_traveled = Double.parseDouble(tokens[8]);
                    Trip trip = new Trip(trip_id, arrival_time, departure_time, stop_id, stop_sequence, stop_headsign, pickup_type, drop_off_type, shape_dist_traveled);
                    // convert arrivalTime of current line to seconds
                    int arrivalInSeconds = timeStringToSeconds(arrival_time);
                    if (trip_id == previousID) {
                        tempList.push(trip);
                        if (arrivalInSeconds == inputArrivalTimeInSeconds)
                            matchesArrivalTime = true;
                    } else {
                        if (matchesArrivalTime) {
                            if (tempList.size() > 0)
                                trips.put(previousID, tempList);
                        }
                        tempList = new LinkedList<>();
                        tempList.push(trip);
                        previousID = trip_id;
                        matchesArrivalTime = false;
                    }

                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();

        }
    }

    static int timeStringToSeconds(String time) {
        try {
            String[] arrivalTokens = time.split(":");
            arrivalTokens[0] = arrivalTokens[0].trim().replaceAll("00", "0");
            arrivalTokens[1] = arrivalTokens[1].trim().replaceAll("00", "0");
            arrivalTokens[2] = arrivalTokens[2].trim().replaceAll("00", "0");
            int arrivalH = Integer.parseInt(arrivalTokens[0]);
            int arrivalM = Integer.parseInt(arrivalTokens[1]);
            int arrivalS = Integer.parseInt(arrivalTokens[2]);
            int calculatedTime = arrivalS + (arrivalM * 60) + (arrivalH * 60 * 60);
            return calculatedTime;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace(System.out);
            return -1;
        }
    }

}
