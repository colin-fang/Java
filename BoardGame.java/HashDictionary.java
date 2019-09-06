/*
 * Written by Colin Fang
 */
public class HashDictionary implements DictionaryADT {

	public HashNode[] nodeArray;
	int size;

	public HashDictionary(int size) {
		this.size = size;
		nodeArray = new HashNode[this.size];
		for (int i = 0; i < this.size; i++) {
			nodeArray[i] = new HashNode(); // Initialize the node Array and fill it with empty Hashnodes
		}

	}

	// Generate key using Horner's Law
	private int generateKey(String config) {
		int key = 0;
		for (int i = 0; i < config.length(); i++) {
			key = ((37 * key) + config.charAt(i)) % this.size;
		}
		return key;
	}

	public int put(Configuration data) throws DictionaryException {
		String config = data.getStringConfiguration();
		int newKey = generateKey(config);
		HashNode newHash = new HashNode(data);
		HashNode thisNode = nodeArray[newKey];
		if (nodeArray[newKey].getState() == null) {
			nodeArray[newKey] = newHash;
			return 0;
		} else {
			// iterate until there are no nodes left in the separate chain or a duplicate is
			// found
			while (thisNode != null && thisNode.getState() != newHash.getState()) {
				thisNode = thisNode.getNext();
			}
			// If there is no next node
			if (thisNode == null) {
				nodeArray[newKey] = newHash;
				return 1;
			}
			// If a duplicate is found
			if (thisNode.getState() == newHash.getState()) {
				throw new DictionaryException("this board configuration already exists!");
			} else { //
				thisNode = newHash;
				return 1;
			}
		}
	}

	public void remove(String config) throws DictionaryException {
		int newKey = generateKey(config);
		HashNode thisNode = nodeArray[newKey];
		HashNode lastNode = new HashNode();
		// iterate until no nodes are left, or the target is found
		while (thisNode != null && thisNode.getState() != config) {
			lastNode = thisNode;
			thisNode = thisNode.getNext();
		}
		if (thisNode == null) {
			throw new DictionaryException("this board configuration doesn't exist!");
		} else { // if the target is between two nodes, link the previous node to the next node
					// to skip the current node
			lastNode.setNext(thisNode.getNext());
		}

	}

	public int getScore(String config) {
		int newKey = generateKey(config);
		return nodeArray[newKey].getScore();
	}
}