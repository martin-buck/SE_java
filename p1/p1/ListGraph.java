import java.util.*;

public class ListGraph implements Graph {
    // this is the no-argument constructor syntax. why is it private?
    private HashMap<String, LinkedList<String>> nodes = new HashMap<>();

    public boolean addNode(String n) {
	    boolean added = false;
        if (!this.hasNode(n)){
            nodes.put(n, new LinkedList<String>());
            added = true;
        }
        return added;
    }

    public boolean addEdge(String n1, String n2) {
	     boolean added = true;

         if (this.hasNode(n1) && this.hasNode(n2)){
            for (String n : nodes.get(n1)) {
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
	     return nodes.containsKey(n);
    }

    public boolean hasEdge(String n1, String n2) {
        boolean nodeFlag = false;
        if (this.hasNode(n1) && this.hasNode(n2)){
            for (String n : nodes.get(n1)) {
                if (n.equals(n2)){
                    nodeFlag = true;
                }
            }
        }
        return nodeFlag;
    }

    public boolean removeNode(String n) {
	     if (this.hasNode(n)){
            nodes.remove(n);
            for (String n1 : nodes.keySet()){
                nodes.get(n1).remove(n);
            }
            return true;
         }
         else{
            return false;
         }
    }

    public boolean removeEdge(String n1, String n2) {
	    if (this.hasEdge(n1, n2)){
            for (String n : nodes.get(n1)) {
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
        throw new UnsupportedOperationException();
    }

    public List<String> succ(String n) {
	    if (this.hasNode(n)){
            return nodes.get(n);
        }
        else{
            throw new NoSuchElementException();
        }
    }

    public List<String> pred(String n) {
	     throw new UnsupportedOperationException();
    }

    public Graph union(Graph g) {
	     throw new UnsupportedOperationException();
    }

    public Graph subGraph(Set<String> nodes) {
	     throw new UnsupportedOperationException();
    }

    public boolean connected(String n1, String n2) {
	     throw new UnsupportedOperationException();
    }
}
