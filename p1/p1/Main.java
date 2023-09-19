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
    }

    public static void test2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
	Edge e = new Edge("a", "b");
	assert eg.addEdge(e);
	assert eg.hasEdge(e);
    }
    
    public static void main(String[] args) {
	test1();
	// test2();
    }

}