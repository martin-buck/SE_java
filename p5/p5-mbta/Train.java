import java.util.HashMap;

public class Train extends Entity {
  private Train(String name) { super(name); }
  public static HashMap<String, Train> train_map = new HashMap<String, Train>();

  public static Train make(String name) {
    // Change this method!
    if (train_map.containsKey(name)){
        return train_map.get(name);
    } else{
        Train new_train = new Train(name);
        train_map.put(name, new_train);
        return new_train;
    }
  }
}
