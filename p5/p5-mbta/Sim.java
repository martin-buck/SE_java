import java.io.*;
import java.util.HashMap;

public class Sim {

  public static void run_sim(MBTA mbta, Log log) {
    /*
    we have two classes, "T_thread" and "P_thread" which will implement Runnable and therefore have
    a run() method. Each class will have a constructor that takes one of the Train objects or Passenger
    objects. Within the run() method, each object will try to move to the next station or board/deboard
    a train. We will synchronize on the station objects
    */
    HashMap<Thread, Passenger> pass_threads = new HashMap<Thread, Passenger>();
    HashMap<Thread, Train> train_threads = new HashMap<Thread, Train>();
    
    // create new threads for each of the passengers and trains in mbta
    for (Passenger p : mbta.get_pass_state().keySet()){
      Thread thr = new Thread(new P_thread(p, mbta, log));
      pass_threads.put(thr, p);
      thr.start();
    }

    for (Train t : mbta.get_train_journeys().keySet()){
      Thread thr = new Thread(new T_thread(t, mbta, log));
      train_threads.put(thr, t);
      thr.start();
    }

    for (Thread t : pass_threads.keySet()){
      try {
        t.join();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    for (Thread t : train_threads.keySet()){
      try {
        t.join();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("usage: ./sim <config file>");
      System.exit(1);
    }

    MBTA mbta = new MBTA();
    mbta.loadConfig(args[0]);

    Log log = new Log();

    run_sim(mbta, log);

    String s = new LogJson(log).toJson();
    PrintWriter out = new PrintWriter("log.json");
    out.print(s);
    out.close();

    mbta.reset();
    mbta.loadConfig(args[0]);
    Verify.verify(mbta, log);
  }
}
