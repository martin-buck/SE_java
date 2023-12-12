import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class T_thread implements Runnable {
    private Train t; 
    private MBTA mbta;
    private Log log;
    private HashMap<String, List<Station>> mbta_lines;
    private HashMap<Station, ArrayList<Entity>> mbta_state;
    private HashMap<Train, ArrayList<Station>> train_journeys;
    private HashMap<Passenger, HashMap<Station, Boolean>> pass_state;
    private boolean run_thread;
    private Station curr_station = null;
    private Station next_station = null;
    private List<Station> this_line = null;
    private String color = null;
    
    public T_thread(Train t, MBTA mbta, Log log){
        this.t = t;
        this.mbta = mbta;
        this.log = log;
        
        // get the current station and next station to move to
        this.mbta_lines = this.mbta.get_lines();
        this.mbta_state = this.mbta.get_state();
        this.train_journeys = this.mbta.get_train_journeys();
        this.pass_state = this.mbta.get_pass_state();
        this.run_thread = true;

        // get the color of this line
        for (String colors : Train.train_map.keySet()){
            if (Train.train_map.get(colors).equals(this.t)){
                color = colors;
            }
        }
        if (color == null){
            throw new RuntimeException("Could not find this Train line in T_thread in train_map object.");
        }
        
        // get the list of stations for this line
        this.this_line = this.mbta_lines.get(color);
        if (this.this_line == null){
            throw new RuntimeException("Could not find this Train line in T_thread in lines object.");
        }

        for (Train train : this.train_journeys.keySet()){
                if (train.equals(this.t)){
                    if (this.train_journeys.get(train).size() == 0){
                        throw new RuntimeException("train_journeys is empty.");
                    }
                    if (this.train_journeys.get(train).size() > 2){
                        throw new RuntimeException("train_journeys has more than two stations.");
                    }
                    if (this.train_journeys.get(train).size() == 1){
                        throw new RuntimeException("train_journeys has only one station.");
                    } else if(train_journeys.get(train).size() == 2){
                        this.curr_station = this.train_journeys.get(train).get(0);
                        this.next_station = this.train_journeys.get(train).get(1);
                        break;
                    }
                }
            }

        if (this.curr_station == null || this.next_station == null){
            throw new RuntimeException("curr_station or next_station is null in T_thread.");
        }
    }

    // this run() method should try to move the Train to the next station
    public void run(){
        // run a while loop until passengers have reached their destinations; iterate over Booleans
        while (this.run_thread){            
            // move the train from curr_station to next_station by updating mbta_state. Check to make sure there is no train there
            // SYNCHRONIZE ON THIS STATION OR NEXT STATION?
            synchronized (this.next_station){
                //System.out.println("Lock: Train " + this.t + " has lock " + this.next_station);
                boolean next_station_free = true;
                for (Entity e : this.mbta_state.get(this.next_station)){
                    if (e.getClass().equals(Train.class)){
                        //System.out.println(e);
                        next_station_free = false;
                        break;
                    }
                }


                while(!next_station_free){
                    try {
                        //System.out.println("Lock: Train " + this.t + " is waiting and gave up lock " + this.next_station);
                        this.next_station.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException("InterruptedException for next_station.wait() in T_thread: " + e.getMessage());
                    }
                    next_station_free = true;
                    for (Entity e : this.mbta_state.get(this.next_station)){
                        if (e.getClass().equals(Train.class)){
                            //System.out.println(e);
                            next_station_free = false;
                            break;
                        }
                    }
                }

                if (next_station_free){
                    // SYNCHRONIZE HERE?
                    synchronized (curr_station){
                        ArrayList<Entity> curr_station_state = this.mbta_state.get(curr_station);
                        boolean state_curr = curr_station_state.remove(this.t);
                        this.mbta_state.put(curr_station, curr_station_state);
                        this.mbta.set_state(this.mbta_state);
                        if (!state_curr){
                            throw new RuntimeException("Could not remove train from curr_station because it is not in the Entity list.");
                        }
                        curr_station.notifyAll();
                    }
                    ArrayList<Entity> next_station_state = this.mbta_state.get(this.next_station);
                    boolean state_next = next_station_state.add(this.t);
                    this.mbta_state.put(this.next_station, next_station_state);
                    this.mbta.set_state(this.mbta_state);
                    if (!state_next){
                        throw new RuntimeException("Could not add train to curr_station.");
                    }
                    this.log.train_moves(this.t, curr_station, next_station);

                    // update train_journeys
                    ArrayList<Station> new_stations = new ArrayList<Station>();
                    Station curr_station_up = null;
                    Station next_station_up = null;
                    int count = 0;
                    // the train is currently at this.next_station
                    for (Station station : this_line){
                        if (station.equals(this.next_station)){
                            // if we are at the beginning of the line moving up
                            if (count == 0 && curr_station.equals(this_line.get(count + 1))){
                                curr_station_up = next_station;
                                next_station_up = this_line.get(count + 1);
                                break;
                            // if we are at the end of the line moving down
                            } else if (count == this_line.size()-1 && curr_station.equals(this_line.get(count - 1))){
                                curr_station_up = next_station;
                                next_station_up = this_line.get(count - 1);
                                break;
                            } else if (curr_station.equals(this_line.get(count - 1))){
                                curr_station_up = next_station;
                                next_station_up = this_line.get(count + 1);
                                break;
                            } else if (curr_station.equals(this_line.get(count + 1))){
                                curr_station_up = next_station;
                                next_station_up = this_line.get(count - 1);
                                break;
                            }
                        }
                        count = count + 1;
                    }
                    if (curr_station_up == null || next_station_up == null){
                        throw new RuntimeException("Stations failed to update in T_thread.");
                    }
                    new_stations.add(0, curr_station_up);
                    new_stations.add(1, next_station_up);
                    this.train_journeys.put(this.t, new_stations);
                    this.mbta.set_train_journeys(train_journeys);
                    this.next_station.notifyAll();
                    this.curr_station = curr_station_up;
                    this.next_station = next_station_up;

                    //System.out.println("Lock: Train " + this.t + " is waking all other threads using lock " + this.curr_station);
                }
                else{
                    //System.out.println("Lock: Train " + this.t + " is waking all other threads using lock " + this.next_station);
                    this.next_station.notifyAll();
                }
            }
            try {
                //System.out.println("Lock: Train " + this.t + " is sleeping at " + this.curr_station);
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException("Caught an interrupted exception from sleeping thread.");
            }
            
            run_thread = false;
            for (HashMap<Station, Boolean> h : this.pass_state.values()){
                if (h.values().contains(false)){
                    run_thread = true;
                    break;
                }
            }
            //System.out.println(run_thread + " " + t);
        }
    }
}
