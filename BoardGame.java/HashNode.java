
public class HashNode {
	private String config;
	private int score;
	private HashNode next;

	public HashNode(Configuration data){
		this.config = data.getStringConfiguration();
		this.score = data.getScore();
		next = null;
	}
	
	public HashNode() {
		config = null;
		score = -1;
		next = null;
	}
	
	public String getState() {
		return config;
	}
	public int getScore() {
		return score;
	}
	public HashNode getNext() {
		return next;
	}
	
	public void setNext(HashNode newNode) {
		next = newNode;
	}
}