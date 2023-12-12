import java.util.*;

public class DeboardEvent implements Event {
  public final Passenger p; public final Train t; public final Station s;
  public DeboardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof DeboardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " deboards " + t + " at " + s;
  }
  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    HashMap<String, List<Station>> deboard_journeys = mbta.get_journeys();
    HashMap<Station, ArrayList<Entity>> mbta_state = mbta.get_state();
    HashMap<Passenger, HashMap<Station, Boolean>> pass_state = mbta.get_pass_state();
    HashMap<Train, ArrayList<Passenger>> train_state = mbta.get_train_state();

    // if this station doesn't have the passenger or the train throw an exception
    if (!mbta_state.get(this.s).contains(this.t)){
      throw new RuntimeException("Train not at this Station.");
    }

    // get this Passenger p's journey
    String p_str = null;
    for (String s1 : Passenger.pass_map.keySet()){
      if (Passenger.pass_map.get(s1).equals(this.p)){
        p_str = s1;
      }
    }

    if (p_str == null){
      throw new RuntimeException("Passenger not in list of journeys!");
    }

    List<Station> p_stations = deboard_journeys.get(p_str);
    if (!p_stations.contains(this.s)){
      throw new RuntimeException("Deboarding station not on this passenger's journey.");
    }
    
    // get the next false station where the passenger should deboard
    Station next_station = null;
    List<Station> journey = mbta.get_journeys().get(this.p.toString());
    for (Station station : journey){
        if (!pass_state.get(this.p).get(station)){
            next_station = station;
            break;
        }
    }

    if (!this.s.equals(next_station)){
      throw new RuntimeException("Passenger should not deboard this train at this station: " + next_station);
    } else if (next_station == null){
      throw new RuntimeException("next_station is null.");
    } else{
          // remove passenger from train
          ArrayList<Passenger> update_train = train_state.get(this.t);
          update_train.remove(this.p);
          train_state.put(this.t, update_train);
          // update pass_state
          HashMap<Station, Boolean> update = pass_state.get(this.p);
          update.put(this.s, true);
          pass_state.put(this.p, update);
          // update waiting at current station
          ArrayList<Entity> waiting = mbta_state.get(this.s);
          waiting.add(this.p);
          mbta_state.put(next_station, waiting);
    }

    mbta.set_train_state(train_state);
    mbta.set_state(mbta_state);
    mbta.set_pass_state(pass_state);
  }
}
