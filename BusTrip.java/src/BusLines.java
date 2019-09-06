import java.io.*;
import java.util.*;

public class BusLines {
	//GLOBAL VARIABLES
	// ------------------------------------------------------
	Graph busMap; // Graph representation of the map
	// Three initializing values, width, length and number of transfers
	int nodesPerRow;
	int nodesPerColumn;
	int trans; // number of allowed busLine transfers
	int start; // index in the stop array where the start node is found
	int end; // ^destination
	//--------------------------------------------------------

	BusLines(String inputFile) throws MapException {
		try {
			//READ FILE AND ASSIGN VARIABLES
			//-------------------------------------------------
			// interpret text file with FIleReader and BufferedReader
			FileReader file = new FileReader(inputFile);
			BufferedReader buff = new BufferedReader(file);

			// Read and parse first line
			String line = buff.readLine();
			Scanner firstLine = new Scanner(line);
			String[] charArray = firstLine.nextLine().split(" ");

			// Assign width, length, and transfer values
			nodesPerRow = Integer.parseInt(charArray[1]);

			int width = nodesPerRow * 2 - 1; // Number of nodes and edges in a row
			nodesPerColumn = Integer.parseInt(charArray[2]);

			int length = nodesPerColumn * 2 - 1; // Number of nodes and edges in a column
			trans = Integer.parseInt(charArray[3]);

			// this double array holds all the string values of the text file
			String stringMap[][] = new String[length][width];

			int busStops = 0; // number of non-house nodes
			int row = 0;
			int column = 0;

			//--------------------------------------------------------------------
			while (row < length) {
				//PUT TEXTFILE INTO STRINGMAP[][]
				//-----------------------------------------------------------------------
				// current line iterates further by one
				line = buff.readLine();
				column = 0;
				while (column < width) {
					char entry = line.charAt(column);
					stringMap[row][column] = Character.toString(entry);// converts the textfile into double array
					if (entry == 'S') {
						start = busStops;
						busStops += 1;
					} else if (entry == 'D') {
						end = busStops;
						busStops += 1;
					} else if (entry == '.') {
						busStops += 1;
					}
					column += 1;
				}
				row += 1;
			}
			//--------------------------------------------------------------------------
			//PUTS STRINGMAP INTO GRAPH BUSMAP
			//--------------------------------------------------------------------------
			busMap = new Graph(busStops); // create new empty graph with busStops number of nodes
			int stopRow = 0; // integer used to find stops in a row in nodeArray busMap.stop
			int stopColumn = 0; // integer used to find stops in a column in nodeArray busMap.stop

			for (int i = 0; i < length; i++) {
				if (i % 2 == 0) { // if i is even, if i is a stop/node
					for (int j = 1; j < width; j += 2) {// j represents the edges

						// takes the entries representing a stop to the left and right of
						// the current edge from the double string array stringMap
						String stop1 = stringMap[i][j - 1];
						String stop2 = stringMap[i][j + 1];

						// the entry that represents the busLine to which the current edge belongs
						char busLine = (stringMap[i][j].charAt(0));

						GraphNode stopOne = null;
						GraphNode stopTwo = null;

						if (stop1.equals("S") || stop1.equals("D") || stop1.equals(".")) {
							stopRow = i / 2 * nodesPerRow + (j - 1) / 2;
							stopOne = busMap.stop[stopRow];
							stopRow += 1;

							if (stop2.equals("S") || stop2.equals("D") || stop2.equals(".")) {
								stopTwo = busMap.stop[stopRow];
							}
							if (busLine != ' ') { // if edge is not a house
								busMap.insertEdge(stopOne, stopTwo, busLine);
							}
						}
					}
				}
				if (i % 2 != 0) {// if i is an edge

					for (int j = 0; j < width; j += 2) {// j represents the edges

						// takes the entries representing a stop to the top and bottom of
						// the current edge from the double string array stringMap
						String stop1 = stringMap[i - 1][j];
						String stop2 = stringMap[i + 1][j];

						// the entry that represents the busLine to which the current edge belongs
						char busLine = (stringMap[i][j]).charAt(0);

						GraphNode stopOne = null;
						GraphNode stopTwo = null;

						if (stop1.equals("S") || stop1.equals("D") || stop1.equals(".")) {
							stopColumn = ((i - 1) / 2 * nodesPerRow) + (j / 2);
							stopOne = busMap.stop[stopColumn];

							if (stopColumn % nodesPerRow + nodesPerRow <= width) {

								// this increments stopColumn so that stopColumn represents the stop directly
								// below it on the busmap
								stopColumn = stopColumn + nodesPerRow;
								if (stop2.equals("S") || stop2.equals("D") || stop2.equals(".")) {
									stopTwo = busMap.stop[stopColumn];
								}
								if (busLine != ' ') { // if edge is not a house

									busMap.insertEdge(stopOne, stopTwo, busLine);
								
								}
							}
						}
						
					}
				}
			}//----------------------------------------------------------------

			firstLine.close();
			buff.close();
		} catch (IOException e) {
			throw new MapException("Error, the file does not exist");
		} catch (GraphException e) {
			System.out.println("Error: could not find edge");
		}
	}

	// Constructor for building a city map with its bus lines from
	// the input file. If the input file does not exist, this method should throw a
	// MapException.
	// Read below to learn about the format of the input file.
	public Graph getGraph() {
		return busMap;
	}

	private Iterator<GraphNode> tripHelper(Stack<GraphNode> stopStack, GraphNode current, GraphNode end, char prev,
			int transfers) throws GraphException {
		// push the current node into the stack and mark it as visited
		current.setMark(true);
		stopStack.push(current);

		// generate an iterator of the edges around the current node
		Iterator<GraphEdge> cross = busMap.incidentEdges(current);

		// check each edge in the iterator
		while (cross.hasNext()) {
			GraphEdge nextEdge = cross.next();

			// this checks if the transfers have exceeded the maximum transfer amount
			// newTransfers takes the place of transfers so that when backtracking, the old
			// transfers value is unchanged
			int newTransfers = transfers;
			if (prev != nextEdge.getBusLine()) {
				if (prev == ' ') {
					;
				} else if (transfers + 1 > trans) {
					continue;
				} else {
					newTransfers += 1;
				}
			}

			// if the next bus stop has not been visited, assign it to nextNode and mark it,
			// otherwise, check next edge in the iterator
			GraphNode nextNode = null;
			if (nextEdge.firstEndpoint() == current && nextEdge.secondEndpoint().getMark() == false) {
				nextNode = nextEdge.secondEndpoint();
			} else if (nextEdge.secondEndpoint() == current && nextEdge.firstEndpoint().getMark() == false) {
				nextNode = nextEdge.firstEndpoint();
			} else {
				continue;
			}

			// if the nextNode is the destination, push it into the stack, generate an
			// iterator for the stack and return it
			if (nextNode.getName() == end.getName()) {
				stopStack.push(nextNode);
				Iterator<GraphNode> plan = stopStack.iterator();
				return plan;
			} else {
				// if the next node is not visited and is not the destination, call the helper
				// function on it
				Iterator<GraphNode> temp = tripHelper(stopStack, nextNode, end, nextEdge.getBusLine(), newTransfers);
				if (temp != null) {
					return temp;
				}
			}
		}
		// this removes a node from the stack if the node is a dead end and unmarks it
		current.setMark(false);
		stopStack.pop();
		return null;

	}

	public Iterator<GraphNode> trip() throws GraphException {
		// This is the stack that will be used to create the iterator
		Stack<GraphNode> stopStack = new Stack<GraphNode>();
		// Initialize the busLine value as ' ', a check will be included in the helper
		// function to skip this
		char prev = ' ';
		// Call the helper function, passing in the stack, the start node, end node,
		// busLine character and number of transfers already used
		Iterator<GraphNode> plan = tripHelper(stopStack, busMap.stop[start], busMap.stop[end], prev, 0);
		return plan;

	}

}
