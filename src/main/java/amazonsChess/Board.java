package amazonsChess;


public class Board {

	protected int[][] board = new int[10][10]; // can change to 2D array based on preference
	public static final int EMPTY = 0;
	public static final int B = 1;
	public static final int W = 2;
	public static final int SPEAR = 3;

	public Board() {
		board = new int[][] {
			{ 0, 0, 0, W, 0, 0, W, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ W, 0, 0, 0, 0, 0, 0, 0, 0, W },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ B, 0, 0, 0, 0, 0, 0, 0, 0, B },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, B, 0, 0, B, 0, 0, 0 },
			};
	}

	public Board(int[][] source) {
		//this();
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				board[i][j] = source[i][j];
			}
	}

	public int[][] getBoard() {
		int[][] tempBoard = new int[10][10];
		for( int i = 0; i < 10; i ++) {
			for( int j = 0; j < 10; j++) {
				tempBoard[j][i] = this.board[j][i];
			}
		}
		return tempBoard;
	}
	
	public int[][] getCloneBoard(){
		return this.board;
	}

	public int getTile(int row, int col) {
		return board[row][col];
	}

	public String getTileSymbol(int row, int col) {
		int tile = getTile(row, col);
		if (tile == EMPTY)
			return ".";
		if (tile == W)
			return "W";
		if (tile == B)
			return "B";
		if (tile == SPEAR)
			return "X";
		return "" + (row * 10 + col + 1);
	}

	// Row column position moves
	public void moveQueen(int prevRow, int prevCol, int nextRow, int nextCol, int player) {
		board[prevRow][prevCol] = EMPTY;
		board[nextRow][nextCol] = player;
	}

	public void throwSpear(int row, int col) {
		board[row][col] = SPEAR;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				sb.append(getTileSymbol(row, col));
			}
			if (row != 9) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	public void printBoard() {
		for(int row = 0; row < 10; row ++) {
			for(int col = 0; col < 10; col ++) {
				System.out.print(getTile(row, col));
			}
			System.out.println();
		}
	}
}
