import java.util.*;

public class EdgeGraphAdapter implements EdgeGraph {

    private Graph g;

    public EdgeGraphAdapter(Graph g) { this.g = g; }

    public boolean addEdge(Edge e) {
      if (this.hasEdge(e)){
        return false;
      }
      else{
        this.g.addNode(e.getSrc());
        this.g.addNode(e.getDst());
        this.g.addEdge(e.getSrc(), e.getDst());
        return true;
      }
    }

    public boolean hasNode(String n) {
	     return this.g.hasNode(n);
    }

    public boolean hasEdge(Edge e) {
	     return this.g.hasEdge(e.getSrc(), e.getDst());
    }

    public boolean removeEdge(Edge e) {
	     if (this.hasEdge(e)){
        this.g.removeEdge(e.getSrc(), e.getDst());
        if (this.g.succ(e.getSrc()).isEmpty()){
          this.g.removeNode(e.getSrc());
        }
        if (this.g.succ(e.getDst()).isEmpty()){
          this.g.removeNode(e.getDst());
        }
        return true;
       }
       else{
        return false;
       }
    }

    public List<Edge> outEdges(String n) {
      List<Edge> edgeList = new ArrayList<Edge>();
      for (String s : this.g.succ(n)){
        Edge edge = new Edge(n, s);
        edgeList.add(edge);
      }
      return edgeList;
    }

    public List<Edge> inEdges(String n) {
      List<Edge> edgeList = new ArrayList<Edge>();
      for (String s : this.g.pred(n)){
        Edge edge = new Edge(s, n);
        edgeList.add(edge);
      }
      return edgeList;
    }

    public List<Edge> edges() {
      List<Edge> edgeList = new ArrayList<Edge>();

      // only need to iterate over in-edges
      for (String s : this.g.nodes()){
        for (Edge e : this.inEdges(s)){
          edgeList.add(e);
        }
      }

      return edgeList;
    }

    public EdgeGraph union(EdgeGraph g) {
      EdgeGraph f = new EdgeGraphAdapter(new ListGraph());

      for (Edge e : this.edges()){
          f.addEdge(e);
      }

      for (Edge e : g.edges()){
          f.addEdge(e);
      }

      return f;
    }

    public boolean hasPath(List<Edge> e) {
	     if (e.isEmpty()){
        return true;
       }
       else if (!this.edges().containsAll(e)){
        return false;
       }
       else{
        Iterator<Edge> iter = e.iterator();
        Edge i = iter.next();
        while(iter.hasNext()){
          Edge j = iter.next();
          if (i.getDst() != j.getSrc()){
            throw new BadPath();
          }
        }
        return true;
       }
    }

}
