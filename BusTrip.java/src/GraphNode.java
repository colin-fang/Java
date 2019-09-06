
public class GraphNode {
	int name;
	boolean mark;

	GraphNode(int name) {
		this.name = name;
		this.mark = false;
	}

	void setMark(boolean mark) {
		this.mark = mark;
	}

	boolean getMark() {
		return this.mark;
	}

	int getName() {
		return this.name;
	}

}
