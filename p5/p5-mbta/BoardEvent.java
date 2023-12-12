import java.util.*;

public class BoardEvent implements Event {
  public final Passenger p; public final Train t; public final Station s;
  public BoardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof BoardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " boards " + t + " at " + s;
  }
  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    // check to make sure that the Passenger and Train are at this station
    HashMap<Station, ArrayList<Entity>> mbta_state = mbta.get_state();
    HashMap<Passenger, HashMap<Station, Boolean>> pass_state = mbta.get_pass_state();
    HashMap<Train, ArrayList<Passenger>> train_state = mbta.get_train_state();

    // this will only pass if the train and the passenger are at the same station
    if (!mbta_state.get(s).contains(p) || !mbta_state.get(s).contains(t)){
      throw new RuntimeException("Passenger or Train not at this Station.");
    }

    // true means passenger has been at this station
    Station next_station = null;
    List<Station> journey = mbta.get_journeys().get(this.p.toString());
    for (Station station : journey){
        if (!pass_state.get(this.p).get(station)){
            next_station = station;
            break;
        }
    }

    // get list of stations on this train's line
    String line_str = null;
    for (String s1:Train.train_map.keySet()){
      if (Train.train_map.get(s1).equals(t)){
        line_str = s1;
        break;
      }
    }
    if (line_str == null){
      throw new RuntimeException("Cannot find name of train line in train_map.");
    }
    List<Station> curr_line = mbta.get_lines().get(line_str);

    // if next_station == null, then we have finished the journey
    if (next_station != null){
      // if next_station is not on the current train's line, do not board
      if (!curr_line.contains(next_station)){
        throw new RuntimeException("Passenger should not board this train at this station.");
      } else{
            // remove passenger from waiting list for this station
            ArrayList<Entity> waiting = mbta_state.get(s);
            waiting.remove(p);
            mbta_state.put(s, waiting);
            // board the train
            ArrayList<Passenger> update_train = train_state.get(t);
            update_train.add(p);
            train_state.put(t, update_train);
      }
    }

    mbta.set_train_state(train_state);
    mbta.set_state(mbta_state);
  }
}
