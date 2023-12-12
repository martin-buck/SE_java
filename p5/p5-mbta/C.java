import java.util.List;
import java.util.Map;
import java.util.*;

import com.google.gson.*;

public class C {
    public HashMap<String, List<String>> lines = new HashMap<String, List<String>>();
    public HashMap<String, List<String>> trips = new HashMap<String, List<String>>();

    public static void main(String[] args) {
      Gson gson = new Gson();
      C c = new C();
      List<String> lines_list = new ArrayList<String>();
      lines_list.add("Davis");
      lines_list.add("Harvard");
      c.lines.put("red", lines_list);
      c.lines.put("orange", lines_list);
      // c.lines = Map.of("red", List.of("Davis", "Harvard", "Kendall", "Park", "Downtown Crossing",
      //       "South Station", "Broadway", "Andrew", "JFK"), "orange", List.of("Ruggles", "Back Bay", "Tufts Medical Center", "Chinatown",
      //       "Downtown Crossing", "State", "North Station",  "Sullivan"));
      c.trips.put("Bob", lines_list);
      c.trips.put("Alice", lines_list);
      String s = gson.toJson(c);
      System.out.println(s);

      C c2 = gson.fromJson(s, C.class);
      System.out.println(c2.lines);
      System.out.println(c2.trips);
    }
}
