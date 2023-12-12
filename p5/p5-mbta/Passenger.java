import java.util.HashMap;

public class Passenger extends Entity {
  private Passenger(String name) { super(name); }
  public static HashMap<String, Passenger> pass_map = new HashMap<String, Passenger>();

  public static Passenger make(String name) {
    // Change this method!
    if (pass_map.containsKey(name)){
      return pass_map.get(name);
    } else{
        Passenger new_pass = new Passenger(name);
        pass_map.put(name, new_pass);
        return new_pass;
    }
  }
}
