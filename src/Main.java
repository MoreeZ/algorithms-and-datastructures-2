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
                // Check if input has integer format. Catch error if not int format
                int option = Integer.parseInt(in);
                switch (option) {
                    case 1:
                        System.out.println("Loading bus journey information...");
                        runShortestPath(input, stops);
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
                // If not int format then check if it says "quit" if quit then quit.
                if (in.equalsIgnoreCase("quit")) {
                    System.out.print("Quitting program. See you later!");
                    quit = true;
                } else {
                    // if not quit then tell user that the input is incorrect.
                    System.out.println("Incorrect input! Please enter a number.");
//                    e.printStackTrace(System.out); // debug
                }
            }
        }
        input.close();

    }

    static void runShortestPath(Scanner input, HashMap<Integer, Stop> stops) {
        // Initialise and fill all a map that maps the
        HashMap<Integer, Integer> IDtoIndexMap = new HashMap<>();
        int index = 0;
        for (Map.Entry<Integer, Stop> set : stops.entrySet()) {
            IDtoIndexMap.put(set.getKey(), index++);
        }
        // Initialise a list of all edges for the dijkstra algorithm
        ArrayList<DijkstraSearch.Edge> edgeList = getEdgeList(IDtoIndexMap);
        DijkstraSearch dijkstra = new DijkstraSearch(edgeList, stops);
        boolean exit = false;
        while (!exit) {
            System.out.println("=========== FIND SHORTEST PATH ==========");
            System.out.println("Enter the departure bus stop and the\n" +
                    "destination bus stop to find the shortest\n" +
                    "path between the stops.");
            System.out.println("Type \"exit\" to exit.");
            System.out.println("=========================================");
            System.out.print("Enter departure stop ID: ");
            int inputFrom, inputTo = -1;
            String searchInput = input.nextLine();

            inputFrom = validateBusIdInput(searchInput, stops);
            if (searchInput.equalsIgnoreCase("exit")) {
                // if input is "exit" then exit to main menu
                exit = true;
            } else if (inputFrom == -1) {
                // if input is not valid then ask again.
                continue;
            } else {
                // if from input is valid then ask for second "to" input
                System.out.print("Enter arrival stop ID: ");
                searchInput = input.nextLine();
                inputTo = validateBusIdInput(searchInput, stops);
                // if to input is valid then preform the shortest path else go back to start of loop
                if (inputTo == -1) {
                    continue;
                }
                // Get a list of unique edges using trip data and map them to their trip id's
                double distance = dijkstra.getDistance(IDtoIndexMap.get(inputFrom), IDtoIndexMap.get(inputTo));
                System.out.println("Distance from stop " + inputFrom + " to stop " + inputTo + " is " + distance);
                System.out.println("Path from stop " + inputFrom + " to stop " + inputTo + "will be displayed as follows:");
                System.out.println("[step_number]stop_name(stop_id) - distance: distance from last stop");
                // Implement functionality of tracing back the shortest path.
                LinkedList<Integer> path = new LinkedList<>();
                path.addFirst(inputTo);
                int prevVer = dijkstra.previousArr[inputTo];
                while (prevVer != -1) {
                    path.addFirst(prevVer);
                    prevVer = dijkstra.previousArr[prevVer];
                }
                for (int i = 0; i < path.size(); i++) {
                    int stopIdFromIndex = (int) stops.keySet().toArray()[path.get(i)];
                    Stop validStop = stops.get(stopIdFromIndex);
                    double distanceToStop = dijkstra.distanceArr[path.get(i)];
                    System.out.println("[" + i + "]" + validStop.stop_name + "(" + validStop.stop_id + ") - distance: " + distanceToStop);
                }
                System.out.println("Press any key to continue. Enter \"exit\" to go back to main menu");
                if (searchInput.equalsIgnoreCase("exit"))
                    exit = true;
            }
        }
    }

    private static int validateBusIdInput(String searchInput, HashMap<Integer, Stop> stops) {
        try {
            int parsed = Integer.parseInt(searchInput);
            Stop test = stops.get(parsed);
            if (stops.containsKey(parsed)) {
                return parsed;
            } else {
                System.out.println("Bus stop ID does not exist! Please try again.");
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Your input must be an integer. Please try again.");
            return -1;
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

    static ArrayList<DijkstraSearch.Edge> getEdgeList(HashMap<Integer, Integer> IDtoIndexMap) {
        ArrayList<DijkstraSearch.Edge> edgeList = new ArrayList<>();
        String stopTimesStr = "stop_times.txt";
        try {
            // Get file from directory
            File myFile = new File(stopTimesStr);
            Scanner myReader = new Scanner(myFile);
            String dataLine = myReader.nextLine(); // get first line that doesnt store data

            TripStop lastTrip = null;
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
                    TripStop trip = new TripStop(trip_id, arrival_time, departure_time, stop_id, stop_sequence, stop_headsign, pickup_type, drop_off_type, shape_dist_traveled);
                    // convert arrivalTime of current line to seconds
                    if (trip.stop_sequence != 1) {
                        // If not first trip in sequence
                        if (lastTrip != null) {
//                            double weight = timeStringToSeconds(lastTrip.departure_time) - timeStringToSeconds(trip.arrival_time);
                            DijkstraSearch.Edge e = new DijkstraSearch.Edge(IDtoIndexMap.get(lastTrip.stop_id), IDtoIndexMap.get(trip.stop_id), 1);
                            edgeList.add(e);
                        }
                    }
                    lastTrip = trip;

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
        // LOAD FROM TRANSFERS.txt
        String transfersStr = "transfers.txt";
        try {
            // Get file from directory
            File myFile = new File(transfersStr);
            Scanner myReader = new Scanner(myFile);
            String dataLine = myReader.nextLine(); // get first line that doesnt store data

//            from_stop_id,to_stop_id,transfer_type,min_transfer_time
            while (myReader.hasNextLine()) {
                try {
                    // Load a line of text data
                    dataLine = myReader.nextLine();
                    // split the line onto an array
                    String[] tokens = dataLine.split(",");
                    if (tokens.length < 4) {
                        String[] newTokens = new String[4];
                        for (int i = 0; i < tokens.length; i++) {
                            newTokens[i] = tokens[i];
                        }
                        tokens = newTokens;
                    }
                    // place all data values into variables
                    int transFrom = Integer.parseInt(tokens[0]);
                    int transTo = Integer.parseInt(tokens[1]);
                    int TransType = Integer.parseInt(tokens[2]);
                    double transTime;
                    if (tokens[3] == null)
                        transTime = 0.0;
                    else
                        transTime = Double.parseDouble(tokens[3]);

                    double weight = 2.0;
                    if (TransType == 2) {
                        weight = transTime / 100.0;
                    }
                    DijkstraSearch.Edge e = new DijkstraSearch.Edge(IDtoIndexMap.get(transFrom), IDtoIndexMap.get(transTo), weight);
                    edgeList.add(e);


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
        return edgeList;
    }

    static void runSearchTrips(Scanner input, HashMap<Integer, Stop> stops) {
        HashMap<Integer, LinkedList<TripStop>> trips = new HashMap<>();

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
                    for (Map.Entry<Integer, LinkedList<TripStop>> set : trips.entrySet()) {
                        Stop arrivalStop = new Stop();
                        for (int i = 0; i < set.getValue().size(); i++) {
                            if (timeStringToSeconds(set.getValue().get(i).arrival_time) == timeStringToSeconds(arrivalTimeSearchIn)) {
                                arrivalStop = stops.get(set.getValue().get(i).stop_id);
                            }
                        }
                        System.out.println("Trip ID: " + set.getKey() +
                                ".\nLeaving from stop: " + stops.get(set.getValue().getFirst().stop_id).stop_name + "(" + set.getValue().getFirst().stop_id + ")" +
                                ".\nEnding at stop: " + stops.get(set.getValue().getLast().stop_id).stop_name + "(" + set.getValue().getLast().stop_id + ")" +
                                "\nArriving at " + arrivalTimeSearchIn + " at stop: " + arrivalStop.stop_name + "(" + arrivalStop.stop_id + ")");
                        System.out.println("=========================================");
                    }
                } else
                    System.out.println("Invalid input format!");
            }
        }
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

    static void loadRelevantPaths(HashMap<Integer, LinkedList<TripStop>> trips, HashMap<Integer, Stop> stops, String filename, String inputStr) {
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
            LinkedList<TripStop> tempList = new LinkedList<>();
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
                    TripStop trip = new TripStop(trip_id, arrival_time, departure_time, stop_id, stop_sequence, stop_headsign, pickup_type, drop_off_type, shape_dist_traveled);
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
