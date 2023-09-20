import java.util.*;

public class ListGraph implements Graph {
    // this is the no-argument constructor syntax for the HashMap class
    // it is a private field
    private HashMap<String, LinkedList<String>> nodes = new HashMap<>();

    public boolean addNode(String n) {
	    boolean added = false;
        if (!this.hasNode(n)){
            this.nodes.put(n, new LinkedList<String>());
            added = true;
        }
        return added;
    }

    public boolean addEdge(String n1, String n2) {
	     boolean added = true;

         if (this.hasNode(n1) && this.hasNode(n2)){
            for (String n : this.nodes.get(n1)) {
                if (n.equals(n2)){
                    added = false;
                }
            }
            if (added){
                nodes.get(n1).add(n2);
            }
         }
         else{
            throw new NoSuchElementException();
         }
         return added;
    }

    public boolean hasNode(String n) {
	     return this.nodes.containsKey(n);
    }

    public boolean hasEdge(String n1, String n2) {
        boolean nodeFlag = false;

        if (this.hasNode(n1) && this.hasNode(n2)){
            for (String n : this.nodes.get(n1)) {
                if (n.equals(n2)){
                    nodeFlag = true;
                }
            }
        }
        return nodeFlag;
    }

    public boolean removeNode(String n) {
	     if (this.hasNode(n)){
            this.nodes.remove(n);
            for (String n1 : this.nodes.keySet()){
                this.nodes.get(n1).remove(n);
            }
            return true;
         }
         else{
            return false;
         }
    }

    public boolean removeEdge(String n1, String n2) {
	    if (this.hasEdge(n1, n2)){
            for (String n : this.nodes.get(n1)) {
                if (n.equals(n2)){
                    nodes.get(n1).remove(n);
                    return true;
                }
            }
            return false;
        }
        else{
            throw new NoSuchElementException();
        }
    }

    public List<String> nodes() {
        List<String> list = new LinkedList<String>();

        for (String n1 : this.nodes.keySet()){
            list.add(n1);
        }

        return list;
    }

    public List<String> succ(String n) {
	    if (this.hasNode(n)){
            return this.nodes.get(n);
        }
        else{
            throw new NoSuchElementException();
        }
    }

    public List<String> pred(String n) {
        List<String> preds = new LinkedList<String>();

        if (this.hasNode(n)){
            // iterate over the linked list for each node
            for (String n1: this.nodes()){
                for (String n2: this.succ(n1)){
                    if (n2.equals(n)){
                        preds.add(n1);
                    }
                }
            }  
        }
        else{
            throw new NoSuchElementException();
        }
        return preds;
    }

    public Graph union(Graph g) {
	    Graph h = new ListGraph();
        
        for (String s : this.nodes()){
            h.addNode(s);
        }
        for (String s : g.nodes()){
            h.addNode(s);
        }

        for (String s1 : this.nodes()){
            for (String s2: this.succ(s1)){
                h.addEdge(s1, s2);
            }
        }
        for (String s1 : g.nodes()){
            for (String s2: g.succ(s1)){
                h.addEdge(s1, s2);
            }
        }
        return h;
    }

    public Graph subGraph(Set<String> nodes) {
	     Graph g = new ListGraph();
         // iterate through current graph objects nodes
         for (String n1 : this.nodes()){
            if (nodes.contains(n1)){
                g.addNode(n1);
                for (String n2 : this.succ(n1)){
                    if (nodes.contains(n2)){
                        g.addEdge(n1, n2);
                    }
                }
            }
         }
         return g;
    }

    public boolean connected(String n1, String n2) {
	    if (n1.equals(n2)){
            return true;
        }
        return false;
    }
}
