import java.util.*;

public class MoveEvent implements Event {
  public final Train t; public final Station s1, s2;
  public MoveEvent(Train t, Station s1, Station s2) {
    this.t = t; this.s1 = s1; this.s2 = s2;
  }
  public boolean equals(Object o) {
    if (o instanceof MoveEvent e) {
      return t.equals(e.t) && s1.equals(e.s1) && s2.equals(e.s2);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(t, s1, s2);
  }
  public String toString() {
    return "Train " + t + " moves from " + s1 + " to " + s2;
  }
  public List<String> toStringList() {
    return List.of(t.toString(), s1.toString(), s2.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    HashMap<Station, ArrayList<Entity>> mbta_state = mbta.get_state();
    HashMap<String, List<Station>> lines = mbta.get_lines();
    HashMap<Train, ArrayList<Station>> train_journeys = mbta.get_train_journeys();

    // get color of train
    String train_color = null;
    for (String s : Train.train_map.keySet()){
      if (Train.train_map.get(s).equals(t)){
        train_color = s;
        break;
      }
    }
    if (train_color == null){
      throw new RuntimeException("For some reason the given train color is not in the train_map. Maybe not initalized?");
    }

    List<Station> line = lines.get(train_color);    

    // throw exception if s1 or s2 are not on this line
    if (!line.contains(s1) || !line.contains(s2)){
      throw new RuntimeException("One of the two stations is not on this line.");
    }

    // check to see if there is a train at s2 already
    for (Entity e : mbta_state.get(s2)){
      if (e.getClass().equals(Train.class)){
        throw new RuntimeException("There is a train at the destination already: " + s1 + ",  " + s2);
      }
    }

    if (!s1.equals(train_journeys.get(t).get(0)) || !s2.equals(train_journeys.get(t).get(1))){
      throw new RuntimeException("s1 or s2 does not correspond to entries in train_journeys.");
    }

    // move trains
    ArrayList<Entity> waiting_s1 = mbta_state.get(s1);
    waiting_s1.remove(t);
    mbta_state.put(s1, waiting_s1);
    ArrayList<Entity> waiting_s2 = mbta_state.get(s2);
    waiting_s2.add(t);
    mbta_state.put(s2, waiting_s2);
    mbta.set_state(mbta_state);

    // update train_journeys
    ArrayList<Station> new_stations = new ArrayList<Station>();
    Station curr_station_up = null;
    Station next_station_up = null;
    int count = 0;

    // the train is currently at s2
    for (Station station : line){
        if (station.equals(s2)){
            // if we are at the beginning of the line moving up
            if (count == 0 && s1.equals(line.get(count + 1))){
                curr_station_up = s2;
                next_station_up = line.get(count + 1);
                break;
            // if we are at the end of the line moving down
            } else if (count == line.size()-1 && s1.equals(line.get(count - 1))){
                curr_station_up = s2;
                next_station_up = line.get(count - 1);
                break;
            } else if (s1.equals(line.get(count - 1))){
                curr_station_up = s2;
                next_station_up = line.get(count + 1);
                break;
            } else if (s1.equals(line.get(count + 1))){
                curr_station_up = s2;
                next_station_up = line.get(count - 1);
                break;
            }
        }
        count = count + 1;
    }

    if (curr_station_up == null || next_station_up == null){
        throw new RuntimeException("Stations failed to update in T_thread.");
    }

    // update train_journeys
    new_stations.add(0, curr_station_up);
    new_stations.add(1, next_station_up);
    train_journeys.put(this.t, new_stations);
    mbta.set_train_journeys(train_journeys);
  }
}
