import java.util.*;

public class Main {

    // Run "java -ea Main" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)
    
    public static void test1() {
	Graph g = new ListGraph();
	
	// test addNode, hasNode, addEdge, and hasEdge
	assert g.addNode("a");
	assert g.hasNode("a");
	assert g.addNode("b");
	assert g.hasNode("b");
	assert g.addEdge("a", "b");
	assert g.hasEdge("a", "b");

	// test removeNode without an edge
	assert !g.removeNode("c");
	assert g.addNode("c");
	assert g.removeNode("c");
	assert !g.hasNode("c");

	// test removeNode with an edge
	assert g.addNode("c");
	assert g.addEdge("c", "b");
	assert g.addEdge("b", "c");
	assert g.removeNode("c");
	assert !g.hasNode("c");
	assert !g.hasEdge("c", "b");
	assert !g.hasEdge("b", "c");

	// test removeEdge
	assert g.removeEdge("a", "b");
	assert !g.hasEdge("a", "b");
	assert g.hasNode("a");
	assert g.hasNode("b");

	// test succ and nodes
	assert g.addEdge("a", "b");
	assert g.succ("a").contains("b");
	assert g.addNode("c");
	assert g.addEdge("a", "c");
	assert g.succ("a").contains("c");
	
	// test nodes
	assert g.nodes().contains("a");
	assert g.nodes().contains("b");
	assert g.nodes().contains("c");

	// test pred
	assert g.pred("c").contains("a");
	}

    public static void test2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
	Edge e = new Edge("a", "b");
	assert eg.addEdge(e);
	assert eg.hasEdge(e);
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
		nodeSet.add("d");
		assert g.subGraph(nodeSet).hasNode("a");
		assert g.subGraph(nodeSet).hasNode("d");
		assert g.subGraph(nodeSet).hasEdge("d", "a");
		assert !g.subGraph(nodeSet).hasEdge("a", "d");
	}
    
    public static void main(String[] args) {
	test1();
	// test2();
	test3();
	test4();
    }

}