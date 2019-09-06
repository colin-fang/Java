import java.util.Iterator;
import java.util.ArrayList;
public class Graph implements GraphADT {
	GraphEdge[][] edge; //adjacency matrix that contains graph edges
						//outer array represents the name of the current node
						//inner array represents the name of the nodes the current node is connected to
						//inner array contains the graph edges connecting the current node to the other nodes
	GraphNode[] stop; //array of nodes
	int n;	//n is number of nodes

	Graph(int n) {
		this.n = n;
		stop = new GraphNode[n];
		edge = new GraphEdge[n][n];
		for (int i = 0; i < n; i++) {
			stop[i] = new GraphNode(i);
			for(int j = 0; j < n; j++) {
				edge[i][j] = null;
			}
			
		}
	}

	public void insertEdge(GraphNode nodeu, GraphNode nodev, char busLine) throws GraphException{
		try {
			if (edge[nodeu.getName()][nodev.getName()] == null || edge[nodev.getName()][nodeu.getName()] == null) {
				GraphEdge newEdge = new GraphEdge(nodeu, nodev, busLine);
				edge[nodeu.getName()][nodev.getName()] = newEdge;
				edge[nodev.getName()][nodeu.getName()] = newEdge;
			}
		}catch (Exception e){
			throw new GraphException("Error: node(s) does not exist or edge already in graph");
		}
	}
	/*
	 * Adds to the graph an edge connecting the given vertices. The type of the edge
	 * is as indicated. Throws a GraphException if either node does not exist or if
	 * the edge is already in the graph.
	 */

	public GraphNode getNode(int name) throws GraphException{
		try {
			return stop[name];
		}catch(Exception e){
			throw new GraphException("Error: node doesn not exist");
		}
	}
	/*
	 * Returns the node with the specified name. Throws a GraphException if the node
	 * does not exist.
	 */

	public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException{
		try {
			ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();
			for(int i = 0; i < n; i++) {
				if (edge[u.getName()][i] != null) {
					edges.add(edge[u.getName()][i]);
				}
			}
			if (edges.isEmpty()) {
				return null;
			}
			Iterator<GraphEdge> iter = edges.iterator();
			return iter;
		}catch (Exception e) {
			throw new GraphException("Error: node does not exist");
		}
	}
	/*
	 * Returns a Java Iterator storing all the edges incident on the specified node.
	 * It returns null if the node does not have any edges incident on it. Throws a
	 * GraphException if the node does not exist.
	 */

	public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException{
		try {
			return edge[u.getName()][v.getName()];
		}catch(Exception e) {
			throw new GraphException("Error: edge does not exist or node(s) does not exist");
		}
	}
	/*
	 * Returns the edge connecting the given vertices. Throws a GraphException if
	 * there is no edge connecting the given vertices or if u or v do not exist.
	 */

	public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException{
		try {
			if(edge[u.getName()][v.getName()] != null) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			throw new GraphException("Error: node(s) does not exist");
		}
	}
	/*
	 * Returns true is u and v are adjacent, and false otherwise. It throws a
	 * GraphException if either vertex does not exist.
	 */

}
