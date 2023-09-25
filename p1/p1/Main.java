import java.util.*;

public class Main {

    // Run "java -ea Main" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)
    
    public static void test1() {
	Graph g = new ListGraph();
	
	// test addNode, hasNode, addEdge, and hasEdge. Successfully throws NoSuchElementException
	assert g.addNode("a");
	assert g.hasNode("a");
	assert g.addNode("b");
	assert g.hasNode("b");
	assert g.addEdge("a", "b");
	assert g.hasEdge("a", "b");
	assert !g.addEdge("a", "b");

	// test removeNode without an edge
	assert !g.removeNode("c");
	assert g.addNode("c");
	assert g.addEdge("a", "c");
	assert g.addEdge("c", "b");
	assert g.removeNode("c");
	assert !g.hasNode("c");

	// test removeNode with an edge
	assert g.addNode("c");
	assert g.addEdge("c", "b");
	assert g.addEdge("b", "c");
	assert g.removeNode("c");
	assert !g.removeNode("d");
	assert !g.hasNode("c");
	assert !g.hasEdge("c", "b");
	assert !g.hasEdge("b", "c");

	// test removeEdge. Successfully throws NoSuchElementException
	assert g.removeEdge("a", "b");
	assert !g.hasEdge("a", "b");
	assert !g.removeEdge("b", "a");
	assert g.hasNode("a");
	assert g.hasNode("b");

	// test succ and nodes. Successfully throws NoSuchElementException
	assert g.addEdge("a", "b");
	assert g.succ("a").contains("b");
	assert !g.succ("a").contains("c");
	assert g.addNode("c");
	assert g.addEdge("a", "c");
	assert g.succ("a").contains("c");
	assert g.succ("b").isEmpty();
	
	// test nodes
	assert g.nodes().contains("a");
	assert g.nodes().contains("b");
	assert g.nodes().contains("c");
	assert !g.nodes().contains("d");

	// test pred. Successfully throws NoSuchElementException and deals with self-loops
	assert g.pred("c").contains("a");
	assert !g.pred("c").contains("b");
	assert g.pred("a").isEmpty();
	assert g.addEdge("a", "a");
	assert g.pred("a").contains("a");
	assert g.succ("a").contains("a");
	}

	// test add and remove nodes/edges methods within EdgeGraphAdapter
    public static void test2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
	Edge e = new Edge("a", "b");
	assert eg.addEdge(e);
	assert eg.hasEdge(e);
	assert !eg.addEdge(e);
	assert eg.hasNode("a");
	assert eg.hasNode("b");
	assert !eg.hasNode("c");

	Edge f = new Edge("a", "c");
	Edge h = new Edge("b", "a");

	assert eg.addEdge(f);
	assert eg.hasNode("c");
	assert eg.removeEdge(f);
	assert !eg.hasEdge(f);
	assert eg.hasNode("a");
	assert !eg.hasNode("c");
	}

	// test union on two ListGraph
	public static void test3() {
	Graph g = new ListGraph();
	Graph h = new ListGraph();

	assert g.addNode("a");
	assert g.addNode("b");
	assert g.addNode("c");
	assert g.addEdge("a", "b");
	assert g.addEdge("a", "c");

	assert h.addNode("d");

	assert g.union(h).hasNode("d");
	assert h.union(g).hasNode("d");
	assert g.union(h).hasEdge("a", "b");
	}

	// test union on a general Graph
	public static void test4(){
		Graph g = new ListGraph();
		
		assert g.addNode("a");
		assert g.addNode("b");
		assert g.addNode("c");
		assert g.addNode("d");
		assert g.addEdge("a", "b");
		assert g.addEdge("a", "c");
		assert g.addEdge("c", "d");
		assert g.addEdge("d", "a");

		HashSet<String> nodeSet = new HashSet<String>();
		nodeSet.add("a");
		nodeSet.add("c");
		assert g.subGraph(nodeSet).hasNode("a");
		assert g.subGraph(nodeSet).hasNode("c");
		assert g.subGraph(nodeSet).hasEdge("a", "c");
		assert !g.subGraph(nodeSet).hasEdge("a", "d");

		nodeSet.add("d");
		assert g.subGraph(nodeSet).hasEdge("d", "a");
		assert g.subGraph(nodeSet).hasEdge("c", "d");
	}
	
	// test outEdges and inEdges and edges methods for EdgeGraphAdapter
	public static void test5() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
		Edge e = new Edge("a", "b");
		Edge f = new Edge("a", "c");
		Edge h = new Edge("b", "a");
		eg.addEdge(e);
		eg.addEdge(f);
		eg.addEdge(h);

		List<Edge> succs1 = new ArrayList<Edge>();
		List<Edge> preds1 = new ArrayList<Edge>();
		preds1.add(h);
		succs1.add(e);
		succs1.add(f);
		assert eg.outEdges("a").containsAll(succs1);
		assert eg.inEdges("a").containsAll(preds1);
		assert eg.edges().containsAll(succs1);
		assert eg.edges().containsAll(preds1);
	}

	// test hasPath from EdgeGraphAdapter
	public static void test6(){
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
		Edge e = new Edge("a", "b");
		Edge f = new Edge("a", "c");
		Edge h = new Edge("b", "a");
		Edge i = new Edge("c", "d");

		eg.addEdge(e);
		eg.addEdge(f);
		eg.addEdge(h);
		eg.addEdge(i);

		List<Edge> path = new ArrayList<Edge>();
		List<Edge> pathEmpty = new ArrayList<Edge>();
		path.add(f);
		assert eg.hasPath(path);
		assert eg.hasPath(pathEmpty);
		path.add(i);
		assert eg.hasPath(path);
	}

	public static void test7(){
		Graph g = new ListGraph();
		
		assert g.addNode("a");
		assert g.addNode("b");
		assert g.addNode("c");
		assert g.addNode("d");
		assert g.addEdge("a", "b");
		assert g.addEdge("a", "c");
		assert g.addEdge("c", "d");
		assert g.addEdge("d", "a");

		assert g.connected("a", "d");
		assert g.connected("a", "a");
		assert g.connected("d", "c");
		assert !g.connected("b", "c");
		assert !g.connected("b", "d");
	}
	
    
    public static void main(String[] args) {
	test1();
	test2();
	test3();
	test4();
	test5();
	test6();
	test7();
    }

}