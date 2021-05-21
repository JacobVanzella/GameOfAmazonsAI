package amazonsChess;

public class Board {

	protected int[] board; // can change to 2D array based on preference
	public static final int EMPTY = 0;
	public static final int W = 1;
	public static final int B = 2;
	public static final int SPEAR = 3;

	public Board() {
		board = new int[]{
				0,0,0,B,0,0,B,0,0,0,
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,
				B,0,0,0,0,0,0,0,0,B,
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,
				W,0,0,0,0,0,0,0,0,W,
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,
				0,0,0,W,0,0,W,0,0,0,				
				};
	}

	public Board(Board source) {
		this();
		for (int i = 0; i < 100; i++) {
			board[i] = source.board[i];
		}
	}

	public void clone(Board source) {
		for (int i = 0; i < 100; i++) {
			board[i] = source.board[i];
		}
	}

	public int getTile(int row, int col) {
		return board[row * 10 + col];
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
		return "" + (row * 3 + col + 1);
	}

	public void setTile(int row, int col, int value) {
		board[row * 3 + col] = value;
	}

	public void setTileFromIndex(int index, int value) {
		board[index - 1] = value;
	}

	public int checkWin() {
		// needs to be implemented
		return -1;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				sb.append(getTileSymbol(row, col));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
