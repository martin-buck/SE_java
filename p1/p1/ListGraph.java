import java.util.*;

public class ListGraph implements Graph {
    // this is the no-argument constructor syntax for the HashMap class
    // it is a private field
    private HashMap<String, LinkedList<String>> nodes = new HashMap<>();

    // add node to the nodes HashMap representing our graph
    public boolean addNode(String n) {
	    boolean added = false;
        // if n is not present as a key, add with a new empty linked list
        if (!this.hasNode(n)){
            this.nodes.put(n, new LinkedList<String>());
            added = true;
        }
        return added;
    }
    
    // add an edge between two nodes if both are present
    public boolean addEdge(String n1, String n2) {        
        boolean edgeAdded = false;

        // throw an exception if both nodes are not present
         if (this.hasNode(n1) && this.hasNode(n2)){
            boolean edgePresent = this.hasEdge(n1, n2);
            if (!edgePresent){
                nodes.get(n1).add(n2);
                edgeAdded = true;
            }
         }
         else{
            throw new NoSuchElementException();
         }
         return edgeAdded;
    }

    public boolean hasNode(String n) {
	     return this.nodes.containsKey(n);
    }

    public boolean hasEdge(String n1, String n2) {
        boolean nodeFlag = false;

        if (this.hasNode(n1) && this.hasNode(n2)){
            for (String n : this.succ(n1)) {
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
            for (String n1 : this.nodes()){
                this.succ(n1).remove(n);
            }
            return true;
         }
         else{
            return false;
         }
    }

    public boolean removeEdge(String n1, String n2) {
	    if (this.hasNode(n1) && this.hasNode(n2)){
            for (String n : this.succ(n1)) {
                if (n.equals(n2)){
                    this.succ(n1).remove(n);
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

    // create a new graph that has all the nodes and edges of this graph and argument graph
    public Graph union(Graph g) {
	    Graph h = new ListGraph();
        
        // add nodes from both graphs
        for (String s : this.nodes()){
            h.addNode(s);
        }
        for (String s : g.nodes()){
            h.addNode(s);
        }

        // add edges from both graphs
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
                        g.addNode(n2);
                        g.addEdge(n1, n2);
                    }
                }
            }
         }
         return g;
    }

    public boolean connected(String n1, String n2) {
        // if the two strings are equal the designated behavior is return true
	    if (n1.equals(n2)){
            return true;
        }
        // if either node is not in the graph, throw exception
        else if (!this.hasNode(n1) || !this.hasNode(n2)){
            throw new NoSuchElementException();
        }
        // do a DFS to see if there is a path from n1 to n2
        else{
            // a stack and a map indicated if a node has been visited
            ArrayList<String> stack = new ArrayList<String>();
            HashMap<String, String> marked = new HashMap<String, String>();

            // set the initial status for all nodes
            for (String n: this.nodes()){
                marked.put(n, new String());
            }

            // add n1 to the stack and perform the DFS
            stack.add(n1);
            while (!stack.isEmpty()){
                String n = stack.remove(0);
                if (n.equals(n2)){
                    return true;
                }
                else if (!marked.get(n).equals("visited")){
                    marked.put(n, "visited");
                    for (String m : this.succ(n)){
                        stack.add(m);
                    }
                }
            }
        }
        return false;
    }
}
