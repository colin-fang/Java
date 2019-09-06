
public class GraphEdge {
	GraphNode head;
	GraphNode tail;
	char busLine = '?';

	GraphEdge(GraphNode u, GraphNode v, char busLine) {
		this.head = u;
		this.tail = v;
		this.busLine = busLine;
	}

	GraphNode firstEndpoint() {
		return this.head;
	}

	GraphNode secondEndpoint() {
		return this.tail;
	}

	char getBusLine() {
		return this.busLine;
	}

}
