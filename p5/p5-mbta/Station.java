import java.util.HashMap;

public class Station extends Entity {
  private Station(String name) { super(name); }
  public static HashMap<String, Station> station_map = new HashMap<String, Station>();

  public static Station make(String name) {
    // Change this method!
    if (station_map.containsKey(name)){
      return station_map.get(name);
    } else{
        Station new_station = new Station(name);
        station_map.put(name, new_station);
        return new_station;
    }
  }
}
