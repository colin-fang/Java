//Written by Colin Fang
public class BoardGame {
	int boardSize;
	char[][] gameBoard;
	int TABLE_SIZE = 9887;

	public BoardGame(int board_size, int empty_positions, int max_levels) {
		boardSize = board_size;
		gameBoard = new char[board_size][board_size];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				gameBoard[i][j] = 'g';
			}
		}

	}

	public HashDictionary makeDictionary() {
		HashDictionary table = new HashDictionary(TABLE_SIZE);
		return table;
	}

	public int isRepeatedConfig(HashDictionary dict) {
		String state = "";
		// convert gameboard into string
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				state = state + gameBoard[i][j];
			}
		}
		// check if the node is a default node with default score value -1
		if (dict.getScore(state) != -1) {
			return dict.getScore(state);
		} else {
			return -1;
		}

	}

	public void putConfig(HashDictionary dict, int score) {
		String state = "";
		// convert board into string
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				state = state + gameBoard[i][j];
			}
		}
		// if the board configuration is not a duplicate, put it into the hashtable
		if (isRepeatedConfig(dict) == -1) {
			Configuration thisCon = new Configuration(state, score);
			dict.put(thisCon);
		}
	}

	public void savePlay(int row, int col, char symbol) {
		gameBoard[row][col] = symbol;
	}

	public boolean positionIsEmpty(int row, int col) {
		if (gameBoard[row][col] == 'g') {
			return true;
		} else {
			return false;
		}
	}

	public boolean tileOfComputer(int row, int col) {
		if (gameBoard[row][col] == 'o') {
			return true;
		} else {
			return false;
		}
	}

	public boolean tileOfHuman(int row, int col) {
		if (gameBoard[row][col] == 'b') {
			return true;
		} else {
			return false;
		}
	}

	public boolean wins(char symbol) {
		/*
		 * toggle is the variable i use to find non-symbol board pieces. 0 is for all
		 * symbol piece, 1 is for one or more non-symbol pieces is found
		 */
		int toggle = 0;
		/* row check */
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (gameBoard[i][j] != symbol) {
					toggle = 1;
				}
			}
			if (toggle == 0) {
				return true;
			} else {
				toggle = 0;
			}
		}
		/* column check */
		for (int j = 0; j < boardSize; j++) {
			for (int i = 0; i < boardSize; i++) {
				if (gameBoard[i][j] != symbol) {
					toggle = 1;
				}
			}
			if (toggle == 0) {
				return true;
			} else {
				toggle = 0;
			}
		}
		/* top left to bottom right diagonal check */
		for (int i = 0; i < boardSize; i++) {
			if (gameBoard[i][i] != symbol) {
				toggle = 1;
			}

		}
		if (toggle == 0) {
			return true;
		}

		toggle = 0;

		/* top right to bottom left diagonal check */
		for (int i = boardSize - 1; i >= 0; i--) {
			if (gameBoard[i][i] != symbol) {
				toggle = 1;
			}

		}
		if (toggle == 0) {
			return true;
		}
		return false;
	}

	public boolean isDraw(char symbol, int empty_positions) {
		// the number of empty spaces
		int empCount = 0;
		// check for win
		if (wins('o') == true || wins('b') == true) {
			return false;
		}
		// count the number of empty spaces
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (positionIsEmpty(i, j)) {
					empCount = empCount + 1;
				}
			}
		}
		/* check that the boardSize is not full */
		if (empCount == 0) {
			return true;
		}
		// check that all the possible empty positions are not all filled up
		if (empCount > empty_positions) {
			return false;
		}
		/* check that the game is draw if there are empty positions */
		/*
		 * toggle is the variable to check at the end if there are any possible
		 * movements 0 for no possible movements 1 for possible movements found
		 */
		int toggle = 0;
		for (int i = 1; i < boardSize - 1; i++) {
			for (int j = 1; j < boardSize - 1; j++) {
				if (positionIsEmpty(i, j)) {
					if (gameBoard[i - 1][j] == symbol || gameBoard[i - 1][j - 1] == symbol
							|| gameBoard[i - 1][j + 1] == symbol || gameBoard[i + 1][j] == symbol
							|| gameBoard[i + 1][j - 1] == symbol || gameBoard[i + 1][j + 1] == symbol
							|| gameBoard[i][j - 1] == symbol || gameBoard[i][j + 1] == symbol) {
						toggle = 1;
					}
				}
			}
		}
		/*
		 * check for draw for corners corners have 3 possible moves
		 */
		if (positionIsEmpty(0, 0)) {
			if (gameBoard[1][1] == symbol || gameBoard[0][1] == symbol || gameBoard[1][0] == symbol) {
				toggle = 1;
			}
		}
		if (positionIsEmpty(0, boardSize - 1)) {
			if (gameBoard[1][boardSize - 2] == symbol || gameBoard[0][boardSize - 2] == symbol
					|| gameBoard[1][boardSize - 1] == symbol) {
				toggle = 1;
			}
		}
		if (positionIsEmpty(boardSize - 1, 0)) {
			if (gameBoard[boardSize - 2][1] == symbol || gameBoard[boardSize - 1][1] == symbol
					|| gameBoard[boardSize - 2][0] == symbol) {
				toggle = 1;
			}
		}
		if (positionIsEmpty(boardSize - 1, boardSize - 1)) {
			if (gameBoard[boardSize - 2][boardSize - 2] == symbol || gameBoard[boardSize - 1][boardSize - 2] == symbol
					|| gameBoard[boardSize - 2][boardSize - 1] == symbol) {
				toggle = 1;
			}
		}
		/*
		 * check for draw for sides excluding corners the for loops start at the top of
		 * one side (excluding position 0 or position board - 1 because those are
		 * corners) iterates forward each side piece has a total of 5 moves
		 */
		for (int i = 1; i < boardSize - 2; i++) {
			if (gameBoard[i - 1][0] == symbol || gameBoard[i - 1][1] == symbol || gameBoard[i][1] == symbol
					|| gameBoard[i + 1][0] == symbol || gameBoard[i + 1][1] == symbol) {
				toggle = 1;

			}
		}
		for (int i = 1; i < boardSize - 2; i++) {
			if (gameBoard[0][i - 1] == symbol || gameBoard[1][i - 1] == symbol || gameBoard[1][i] == symbol
					|| gameBoard[0][i + 1] == symbol || gameBoard[1][i + 1] == symbol) {
				toggle = 1;

			}
		}
		for (int i = 1; i < boardSize - 2; i++) {
			if (gameBoard[i - 1][boardSize - 1] == symbol || gameBoard[i - 1][boardSize - 2] == symbol
					|| gameBoard[i][boardSize - 2] == symbol || gameBoard[i + 1][boardSize - 1] == symbol
					|| gameBoard[i + 1][boardSize - 2] == symbol) {
				toggle = 1;

			}
		}
		for (int i = 1; i < boardSize - 2; i++) {
			if (gameBoard[boardSize - 1][i - 1] == symbol || gameBoard[boardSize - 2][i - 1] == symbol
					|| gameBoard[boardSize - 2][i] == symbol || gameBoard[boardSize - 1][i + 1] == symbol
					|| gameBoard[boardSize - 2][i + 1] == symbol) {
				toggle = 1;

			}
		}
		if (toggle == 1) {
			return false;
		}
		return true;
	}

	public int evalBoard(char symbol, int empty_positions) {
		if (wins('o')) {
			return 3;
		}
		if (wins('b')) {
			return 0;
		}
		if (isDraw(symbol, empty_positions)) {
			return 2;
		} else {
			return 1;
		}
	}
}