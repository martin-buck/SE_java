import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.*;

public class MBTA {
  // these fields will store train lines and passenger journeys for the current simulation
  private HashMap<String, List<Station>> lines = new HashMap<String, List<Station>>();
  private HashMap<String, List<Station>> journeys = new HashMap<String, List<Station>>();
  // mbta_state keeps track of the status of stations
  private HashMap<Station, ArrayList<Entity>> mbta_state = new HashMap<Station, ArrayList<Entity>>();
  // train_state keeps track of the passengers that are on the trains
  private HashMap<Train, ArrayList<Passenger>> train_state = new HashMap<Train, ArrayList<Passenger>>();
  // this will store a list of length two that stores the current train's station and the next station
  private HashMap<Train, ArrayList<Station>> train_journeys = new HashMap<Train, ArrayList<Station>>();
  // pass_state keeps track of where the passengers are on their journey
  private HashMap<Passenger, HashMap<Station, Boolean>> pass_state = new HashMap<Passenger, HashMap<Station, Boolean>>();

  // Creates an initially empty simulation
  public MBTA() {}

  // getters and setters
  public HashMap<String, List<Station>> get_lines(){
    return this.lines;
  }

  public HashMap<String, List<Station>> get_journeys(){
    return this.journeys;
  }

  public HashMap<Station, ArrayList<Entity>> get_state(){
    return this.mbta_state;
  }

  public void set_state(HashMap<Station, ArrayList<Entity>> mbta_state){
    this.mbta_state = mbta_state;
  }

  public HashMap<Train, ArrayList<Passenger>> get_train_state(){
    return this.train_state;
  }

  public void set_train_state(HashMap<Train, ArrayList<Passenger>> train_state){
    this.train_state = train_state;
  }

  public HashMap<Passenger, HashMap<Station, Boolean>> get_pass_state(){
    return this.pass_state;
  }
  
  public void set_pass_state(HashMap<Passenger, HashMap<Station, Boolean>> pass_state){
    this.pass_state = pass_state;
  }

  public HashMap<Train, ArrayList<Station>> get_train_journeys(){
    return this.train_journeys;
  }

  public void set_train_journeys(HashMap<Train, ArrayList<Station>> train_journeys){
    this.train_journeys = train_journeys;
  }

  // Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) {
    List<Station> stations_objects = new ArrayList<Station>();
    for (String s : stations){
        stations_objects.add(Station.make(s));
    }
    this.lines.put(name, stations_objects);

    // also need to updates states
    int count = 0;
    for (Station t: this.lines.get(name)){
      if (count == 0){
        ArrayList<Entity> first_train = new ArrayList<Entity>();
        ArrayList<Station> first_sec_stops = new ArrayList<Station>();
        first_train.add(Train.make(name));
        first_sec_stops.add(t);
        this.mbta_state.put(t, first_train);
        this.train_journeys.put(Train.make(name), first_sec_stops);
      } else if (count == 1){
        ArrayList<Station> first_sec_stops = this.train_journeys.get(Train.make(name));
        first_sec_stops.add(t);
        this.train_journeys.put(Train.make(name), first_sec_stops);
        this.mbta_state.put(t, new ArrayList<Entity>());
      } 
      else{
        this.mbta_state.put(t, new ArrayList<Entity>());
      }
      count = count + 1;
    }
    
    // We should be guaranteed that count >= 2, as there will always be 2 or more stations on a line
    if (count < 2){
      throw new RuntimeException("Less than two stations on a line?");
    }

    // add train (color) to train_state
    this.train_state.put(Train.make(name), new ArrayList<Passenger>());
  }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) {
    List<Station> stations_objects = new ArrayList<Station>();
    for (String s : stations){
      stations_objects.add(Station.make(s));
    }
    this.journeys.put(name, stations_objects);

    // also need to update states
    int count = 0;
    for (Station t: this.journeys.get(name)){
      if (count == 0){
        ArrayList<Entity> first_stop = mbta_state.get(t);
        first_stop.add(Passenger.make(name));
        this.mbta_state.put(t, first_stop);            
        break;
      } 
    }

    // add the journey to pass_state
    count = 0;
    HashMap<Station, Boolean> to_add = new HashMap<Station, Boolean>();
    for (Station s1: this.journeys.get(name)){
      if (count == 0){
        to_add.put(s1, true);
      } else{
        to_add.put(s1, false);
      }
      count = count + 1;
    }
    this.pass_state.put(Passenger.make(name), to_add);
  }

  // Return normally if initial simulation conditions are satisfied
  public void checkStart() {
    // iterate through the lines and journeys lists and compare to the mbta_state
    for (String s: this.lines.keySet()){
      Station start_station = this.lines.get(s).get(0);
      if (this.mbta_state.get(start_station).contains(Train.make(s))){
        continue;
      } else{
        throw new RuntimeException("Start station doesn't contain train.");
      }
    }

    for (String s: this.journeys.keySet()){
      Station start_station = this.journeys.get(s).get(0);
      if (this.mbta_state.get(start_station).contains(Passenger.make(s))){
        continue;
      } else{
        throw new RuntimeException("Passenger is not at their first station.");
      }
    }
  }

  // Return normally if final simulation conditions are satisfied
  public void checkEnd() {
    for (String s: this.journeys.keySet()){
      int journey_len = this.journeys.get(s).size();
      Station end_station = this.journeys.get(s).get(journey_len-1);
      if (mbta_state.get(end_station).contains(Passenger.make(s))){
        continue;
      } else{
        throw new RuntimeException("A Passenger is not at their final destination.");
      }
    }
  }

  // reset to an empty simulation
  public void reset() {
    this.mbta_state.clear();
    this.train_state.clear();
    this.pass_state.clear();
    this.train_journeys.clear();
  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) {
      Gson gson = new Gson();
      BufferedReader br;
      String json_str;
      try {
          br = new BufferedReader(new FileReader(filename));
      } catch (FileNotFoundException e) {
          throw new RuntimeException(e.getMessage());
      }

      try {
          json_str = new String(Files.readAllBytes(Paths.get(filename)));
      } catch (Exception e) {
          throw new RuntimeException(e.getMessage());
      }
      SimJSON sim = gson.fromJson(json_str, SimJSON.class);      
      
      // this iterates over the keys of the sim.lines_loc which are the train line colors e.g. "red"
      for (String s: sim.lines.keySet()){
        this.addLine(s, sim.lines.get(s));
      }

      // this iterates over the keys of sim.trips_loc which are names of Passengers e.g. "Bob"
      for (String s: sim.trips.keySet()){
        this.addJourney(s, sim.trips.get(s));
      }
  }
}
