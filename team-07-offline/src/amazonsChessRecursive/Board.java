package amazonsChessRecursive;

import java.util.ArrayList;
import java.util.List;

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
	
	public int[] getBoard() {
		return this.board;
	}
	public void updateBoard( int[] newBoard) {
		for( int i = 0 ; i < 100; i ++) {
			board[i] = newBoard[i];
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
		return "" + (row * 10 + col + 1);
	}
	
	//Row column position moves
	public void moveQueen(int prevRow, int prevCol, int nextRow, int nextCol, int player) {
		board[prevRow * 10 + prevCol] = EMPTY;
		board[nextRow * 10 + nextCol] = player;
	}
	public void throwSpear(int row, int col) {
		board[row * 10 + col] = SPEAR;
	}
	//Index position moves
	public void moveQueen(int prevIndex, int nextIndex, int player) {
		board[prevIndex - 1] = EMPTY;
		board[nextIndex - 1] = player;
	}
	public void throwSpear(int index) {
		board[index - 1] = SPEAR;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				sb.append(getTileSymbol(row, col));
			}
			if( row != 9) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public int hasWinner() { // if = 0 no winner, =1 white win, =2 black win
		int hasWinner = 0;

		Board currBoard = new Board();
		List<List<Integer>> blackQ = new ArrayList<List<Integer>>();
		List<List<Integer>> whiteQ = new ArrayList<List<Integer>>();

		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (currBoard.getTile(row, col) == 1) {
					ArrayList<Integer> queenN = new ArrayList<Integer>();
					queenN.add(row);
					queenN.add(col);
					whiteQ.add(queenN);
				} else if (currBoard.getTile(row, col) == 2) {
					ArrayList<Integer> queenN = new ArrayList<Integer>();
					queenN.add(row);
					queenN.add(col);
					blackQ.add(queenN);
				}
			}
		}
		boolean blackCanMove = false;
		for (List<Integer> blackQueen : blackQ) {
			int row = blackQueen.get(0);
			int col = blackQueen.get(1);
			if (row != 0 && col != 9) { // checks if out of board bounds
				if (currBoard.getTile(row - 1, col + 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
					blackCanMove = true;
				}
			}
			if (row != 0) { // checks if out of board bounds
				if (currBoard.getTile(row - 1, col) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					blackCanMove = true;
				}
			}

			if (row != 0 && col != 0) { // checks if out of board bounds
				if (currBoard.getTile(row - 1, col - 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
					blackCanMove = true;
				}
			}
			if (col != 0) { // checks if out of board bounds
				if (currBoard.getTile(row, col - 1) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					blackCanMove = true;
				}
			}
			if (row != 9 && col != 0) { // checks if out of board bounds
				if (currBoard.getTile(row + 1, col - 1) == EMPTY) {
					blackCanMove = true;
				}
			}
			if (row != 9) { // checks if out of board bounds
				if (currBoard.getTile(row + 1, col) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					blackCanMove = true;
				}
			}
			if (row != 9 && col != 9) { // checks if out of board bounds
				if (currBoard.getTile(row + 1, col + 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
					blackCanMove = true;
				}
			}
			if (col != 9) { // checks if out of board bounds
				if (currBoard.getTile(row, col + 1) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					blackCanMove = true;
				}
			}
		}
		boolean whiteCanMove = false;
		for (List<Integer> whiteQueen : whiteQ) {
			int row = whiteQueen.get(0);
			int col = whiteQueen.get(1);
			if (row != 0 && col != 9) { // checks if out of board bounds
				if (currBoard.getTile(row - 1, col + 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
					whiteCanMove = true;
				}
			}
			if (row != 0) { // checks if out of board bounds
				if (currBoard.getTile(row - 1, col) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					whiteCanMove = true;
				}
			}

			if (row != 0 && col != 0) { // checks if out of board bounds
				if (currBoard.getTile(row - 1, col - 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
					whiteCanMove = true;
				}
			}
			if (col != 0) { // checks if out of board bounds
				if (currBoard.getTile(row, col - 1) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					whiteCanMove = true;
				}
			}
			if (row != 9 && col != 0) { // checks if out of board bounds
				if (currBoard.getTile(row + 1, col - 1) == EMPTY) {
					whiteCanMove = true;
				}
			}
			if (row != 9) { // checks if out of board bounds
				if (currBoard.getTile(row + 1, col) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					whiteCanMove = true;
				}
			}
			if (row != 9 && col != 9) { // checks if out of board bounds
				if (currBoard.getTile(row + 1, col + 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
					whiteCanMove = true;
				}
			}
			if (col != 9) { // checks if out of board bounds
				if (currBoard.getTile(row, col + 1) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					whiteCanMove = true;
				}
			}
		}
		if( blackCanMove == false) {
			hasWinner = 1;
		}else if( whiteCanMove == false) {
			hasWinner = 2;
		}
		return hasWinner;
	}
}
