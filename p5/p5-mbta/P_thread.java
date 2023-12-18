import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class P_thread implements Runnable {
    private Passenger p;
    private MBTA mbta;
    private Log log;

    private HashMap<String, List<Station>> mbta_lines;
    private HashMap<Station, ArrayList<Entity>> mbta_state;
    private HashMap<Train, ArrayList<Passenger>> train_state;
    private HashMap<Passenger, HashMap<Station, Boolean>> pass_state;
    private boolean run_thread;
    private Station curr_station = null;
    private Station next_station = null;
    private String train_color = null;
    private Train curr_train = null;

    public P_thread(Passenger p, MBTA mbta, Log log){
        this.p = p;
        this.mbta = mbta;
        this.log = log;
        this.mbta_lines = this.mbta.get_lines();
        this.mbta_state = this.mbta.get_state();
        this.train_state = this.mbta.get_train_state();
        this.pass_state = this.mbta.get_pass_state();
        this.run_thread = true;
        for (Station station : this.mbta_state.keySet()){
            for (Entity e : this.mbta_state.get(station)){
                if (e.equals(p)){
                    this.curr_station = station;
                    break;
                }
            }
            if (this.curr_station != null){
                break;
            }
        }

        if (this.curr_station == null){
            throw new RuntimeException("Cannot initiate curr_station inside P_thread constructor.");
        }
    }

    // this run() method should try to move the Train to the next station
    public void run(){
        // check where the Passenger is on their journey. They can be either on a train or waiting at a station
        // find the current station the passenger is at, if possible
        while (this.run_thread){
            // if the passenger is at this station, find the right train to take them to their next destination
            if (this.curr_station != null){
                synchronized (this.curr_station){
                    //System.out.println("Lock: Passenger " + this.p + " has lock " + this.curr_station);
                    List<Station> journey = this.mbta.get_journeys().get(this.p.toString());
                    for (Station station : journey){
                        if (!this.pass_state.get(this.p).get(station)){
                            this.next_station = station;
                            //System.out.println("Next station for " + this.p + " " +  this.next_station);
                            break;
                        }
                    }

                    if (this.next_station == null){
                        throw new RuntimeException("next_station in P_thread is null.");
                    }

                    // if next station is null, then the passenger has reached their final destination
                    if (this.next_station != null){
                        // if there is a train at this station, check to see if it has next_station on its line
                        for (Entity e: this.mbta_state.get(curr_station)){
                            if (e.getClass().equals(Train.class)){
                                curr_train = (Train) e;
                                for (String color : Train.train_map.keySet()){
                                    if (Train.train_map.get(color).equals(curr_train)){
                                        train_color = color;
                                        break;
                                    }
                                }
                                break;
                            }
                        }

                        // we should always be able to find the train color
                        if (curr_train != null && train_color == null){
                            throw new RuntimeException("Found train but not the train name/color in P_thread!");
                        }

                        while (curr_train == null || (curr_train != null && !mbta_lines.get(train_color).contains(next_station))){
                            try {
                                //System.out.println("Lock: Passenger " + this.p + " is waiting and has released lock " + curr_station);
                                //System.out.println(this.p + " " + curr_train + " " + train_color + " " + next_station);
                                curr_station.wait();
                            } catch (Exception e) {
                                throw new RuntimeException("InterruptedException for next_station.wait() in P_thread: " + e.getMessage());
                            }
                            curr_train = null;
                            train_color = null;
                            for (Entity e: this.mbta_state.get(curr_station)){
                                if (e.getClass().equals(Train.class)){
                                    curr_train = (Train) e;
                                    for (String color : Train.train_map.keySet()){
                                        if (Train.train_map.get(color).equals(curr_train)){
                                            train_color = color;
                                            //System.out.println("Train " + curr_train + " is currently at " + curr_station);
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }

                            // we should always be able to find the train color
                            if (curr_train != null && train_color == null){
                                throw new RuntimeException("Found train but not the train name/color in P_thread!");
                            }
                        }

                        // if there is a train at this station and this train is correct, board the train
                        if (curr_train != null && mbta_lines.get(train_color).contains(next_station)){
                            // remove passenger from list of entities for current station
                            ArrayList<Entity> curr_entity = this.mbta_state.get(curr_station);
                            curr_entity.remove(p);
                            this.mbta_state.put(curr_station, curr_entity);
                            this.mbta.set_state(mbta_state);
                            // add passenger to list of passengers aboard train
                            ArrayList<Passenger> curr_train_pass = train_state.get(curr_train);
                            curr_train_pass.add(p);
                            this.train_state.put(curr_train, curr_train_pass);
                            this.mbta.set_train_state(train_state);
                            this.log.passenger_boards(this.p, curr_train, curr_station);
                            curr_station.notifyAll();
                            // now set curr_station to be null since they are on a train now
                            //System.out.println("Lock: Passenger " + this.p + " is waking threads and has released lock " + this.curr_station);
                            this.curr_station = null;
                        }
                    }
                }
            }
            // if they are on a train, check to see if the train is at the correct station to de-board
            else{
                //System.out.println("Lock: Passenger " + this.p + " has lock " + this.curr_station);
                List<Station> journey = this.mbta.get_journeys().get(this.p.toString());
                for (Station station : journey){
                    if (!this.pass_state.get(this.p).get(station)){
                        this.next_station = station;
                        //System.out.println("Next station for " + this.p + " " +  this.next_station);
                        break;
                    }
                }
                for (Train train : this.train_state.keySet()){
                    if (this.train_state.get(train).contains(p)){
                        curr_train = train;
                        break;
                    }
                }

                // if curr_train is null (and curr_station is null) then we can't find the passenger
                if (curr_train == null){
                    throw new RuntimeException("Passenger cannot be found at a station or on a train!");
                }

                if (next_station == null){
                    throw new RuntimeException("Passenger is on a train but journey is all true.");
                }

                synchronized (this.next_station){
                    boolean deboard_station = this.mbta_state.get(next_station).contains(curr_train);
                    while(!deboard_station){
                        try {
                            this.next_station.wait();
                            //System.out.println("Lock: Passenger " + this.p + " is waiting and has released lock " + next_station);
                        } catch (InterruptedException exc) {
                            throw new RuntimeException("InterruptedException for next_station.wait() in P_thread:"+ exc.getMessage());
                        }
                        deboard_station = this.mbta_state.get(next_station).contains(curr_train);
                    }

                    ArrayList<Entity> curr_entity = mbta_state.get(next_station);
                    curr_entity.add(p);
                    this.mbta_state.put(next_station, curr_entity);
                    this.mbta.set_state(mbta_state);

                    ArrayList<Passenger> curr_train_pass = this.train_state.get(curr_train);
                    curr_train_pass.remove(p);
                    this.train_state.put(curr_train, curr_train_pass);
                    this.mbta.set_train_state(train_state);

                    HashMap<Station, Boolean> curr_pass_journey = pass_state.get(p);
                    curr_pass_journey.put(next_station, true);
                    this.pass_state.put(p, curr_pass_journey);
                    this.mbta.set_pass_state(pass_state);

                    //System.out.println("Lock: Passenger " + this.p + " is waking threads and has released lock " + next_station);
                    this.log.passenger_deboards(this.p, curr_train, next_station);

                    // update current station
                    this.curr_station = this.next_station;
                    // wake up threads in this locks wait set
                    this.next_station.notifyAll();
                }
            }

            // check to see if we should continue running thread because a passenger has not reached their final destination
            if (this.pass_state.get(this.p).values().contains(false)){
                this.run_thread = true;
            } else{
                this.run_thread = false;
            } 
            //System.out.println(this.run_thread + " " + p);
        }
    }
}
