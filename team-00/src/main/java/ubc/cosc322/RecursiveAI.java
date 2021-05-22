package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class RecursiveAI {
	public int[] board;
	public static final int EMPTY = 0;
	public static final int B = 1;
	public static final int W = 2;
	public static final int SPEAR = 3;

	public RecursiveAI(int[] board) {
		this.board = board;
	}
	public int[] cloneBoard() {
		int [] clone = new int[100];
		for( int i = 0; i < 100; i ++) {
			clone[i] = board[i];
		}
		return clone;
	}
	public int getTile( int row, int col) {
		return board[ row * 10 + col];
	}
	public void moveQueen(int prevRow, int prevCol, int nextRow, int nextCol, int player) {
		board[prevRow * 10 + prevCol ] = EMPTY;
		board[nextRow * 10 + nextCol ] = player;
	}
	public void throwSpear(int row, int col) {
		board[row * 10 + col] = SPEAR;
	}
	
	protected List<List<Integer>> fetchPlays( RecursiveAI currBoard, int player){
		List<List<Integer>> moves = currBoard.getMoves(currBoard, player);
		List<List<Integer>> plays = currBoard.getPlays(currBoard, moves, player);
		
		return plays;
	}
	
	protected List<List<Integer>> getPlays(RecursiveAI currBoard, List<List<Integer>> moves, int player){
		List<List<Integer>> plays = new ArrayList<List<Integer>>();
		for( List<Integer> move : moves) {
			RecursiveAI testboard = currBoard;
			testboard.moveQueen(move.get(0), move.get(1), move.get(2), move.get(3), player);
			List<List<Integer>> spearThrow = testboard.getMoves(move.get(2), move.get(3), testboard, player);
			for( List<Integer> throwN : spearThrow) {
				List<Integer> play = new ArrayList<Integer>();
				play.add(move.get(0));
				play.add(move.get(1));
				play.add(move.get(2));
				play.add(move.get(3));
				play.add(throwN.get(2));
				play.add(throwN.get(3));
				plays.add(play);
			}
		}
		return plays;
	}
	
	protected List<List<Integer>> getMoves(RecursiveAI currBoard, int player) {

		// The nested List has form (prevRow, prevCol, nextRow, nextCol) for avaliable
		// moves
		List<List<Integer>> moves = new ArrayList<List<Integer>>(); // will store moves in 2D list

		List<List<Integer>> queenPos = new ArrayList<List<Integer>>();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (currBoard.getTile(row, col) == player) {
					ArrayList<Integer> queenN = new ArrayList<Integer>();
					queenN.add(row);
					queenN.add(col);
					queenPos.add(queenN);
				}
			}
		}
		// Find possible moves for each queen, will sweep from top right CCW
		for (List<Integer> queenCordinates : queenPos) {
			int currRow1 = queenCordinates.get(0);
			int currCol1 = queenCordinates.get(1);
			boolean canMove = true;
			int row = currRow1, col = currCol1;
			while (canMove) { // Direction up right
				if (row != 0 && col != 9) { // checks if out of board bounds
					if (currBoard.getTile(row - 1, col + 1) == EMPTY) { // if in bounds checks up right tile to check if // can move (empty)
						row--;
						col++;
						ArrayList<Integer> move = new ArrayList<Integer>();
						move.add(currRow1);
						move.add(currCol1);
						move.add(row);
						move.add(col);
						moves.add(move);
					} else {
						canMove = false;
					}
				} else {
					canMove = false;
				}
			}
			canMove = true;
			row = currRow1;
			col = currCol1;
			while (canMove) { // Direction up
				if (row != 0) { // checks if out of board bounds
					if (currBoard.getTile(row - 1, col) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
						row--;
						ArrayList<Integer> move = new ArrayList<Integer>();
						move.add(currRow1);
						move.add(currCol1);
						move.add(row);
						move.add(col);
						moves.add(move);
					} else {
						canMove = false;
					}
				} else {
					canMove = false;
				}
			}
			canMove = true;
			row = currRow1;
			col = currCol1;
			while (canMove) { // Direction up left
				if (row != 0 && col != 0) { // checks if out of board bounds
					if (currBoard.getTile(row - 1, col - 1) == EMPTY) { // if in bounds checks up right tile to check if
																		// can move (empty)
						row--;
						col--;
						ArrayList<Integer> move = new ArrayList<Integer>();
						move.add(currRow1);
						move.add(currCol1);
						move.add(row);
						move.add(col);
						moves.add(move);
					} else {
						canMove = false;
					}
				} else {
					canMove = false;
				}
			}
			canMove = true;
			row = currRow1;
			col = currCol1;
			while (canMove) { // Direction left
				if (col != 0) { // checks if out of board bounds
					if (currBoard.getTile(row, col - 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
						col--;
						ArrayList<Integer> move = new ArrayList<Integer>();
						move.add(currRow1);
						move.add(currCol1);
						move.add(row);
						move.add(col);
						moves.add(move);
					} else {
						canMove = false;
					}
				} else {
					canMove = false;
				}
			}
			canMove = true;
			row = currRow1;
			col = currCol1;
			while (canMove) { // Direction down left
				if (row != 9 && col != 0) { // checks if out of board bounds
					if (currBoard.getTile(row + 1, col - 1) == EMPTY) {
						row++;
						col--;
						ArrayList<Integer> move = new ArrayList<Integer>();
						move.add(currRow1);
						move.add(currCol1);
						move.add(row);
						move.add(col);
						moves.add(move);
					} else {
						canMove = false;
					}
				} else {
					canMove = false;
				}
			}
			canMove = true;
			row = currRow1;
			col = currCol1;
			while (canMove) { // Direction down
				if (row != 9) { // checks if out of board bounds
					if (currBoard.getTile(row + 1, col) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
						row++;
						ArrayList<Integer> move = new ArrayList<Integer>();
						move.add(currRow1);
						move.add(currCol1);
						move.add(row);
						move.add(col);
						moves.add(move);
					} else {
						canMove = false;
					}
				} else {
					canMove = false;
				}
			}
			canMove = true;
			row = currRow1;
			col = currCol1;
			while (canMove) { // Direction down right
				if (row != 9 && col != 9) { // checks if out of board bounds
					if (currBoard.getTile(row + 1, col + 1) == EMPTY) { // if in bounds checks up right tile to check if
																		// can move (empty)
						row++;
						col++;
						ArrayList<Integer> move = new ArrayList<Integer>();
						move.add(currRow1);
						move.add(currCol1);
						move.add(row);
						move.add(col);
						moves.add(move);
					} else {
						canMove = false;
					}
				} else {
					canMove = false;
				}
			}
			canMove = true;
			row = currRow1;
			col = currCol1;
			while (canMove) { // Direction right
				if (col != 9) { // checks if out of board bounds
					if (currBoard.getTile(row, col + 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
						col++;
						ArrayList<Integer> move = new ArrayList<Integer>();
						move.add(currRow1);
						move.add(currCol1);
						move.add(row);
						move.add(col);
						moves.add(move);
					} else {
						canMove = false;
					}
				} else {
					canMove = false;
				}
			}
		}
		return moves;
	}

	protected List<List<Integer>> getMoves(int queenRow, int queenCol, RecursiveAI currBoard, int player) {

		// The nested List has form (prevRow, prevCol, nextRow, nextCol) for avaliable
		// moves
		List<List<Integer>> moves = new ArrayList<List<Integer>>(); // will store moves in 2D list

		// Find possible moves for each queen, will sweep from top right CCW
		int currRow1 = queenRow;
		int currCol1 = queenCol;
		boolean canMove = true;
		int row = currRow1, col = currCol1;
		while (canMove) { // Direction up right
			if (row != 0 && col != 9) { // checks if out of board bounds
				if (currBoard.getTile(row - 1, col + 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
					row--;
					col++;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				} else {
					canMove = false;
				}
			} else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1;
		while (canMove) { // Direction up
			if (row != 0) { // checks if out of board bounds
				if (currBoard.getTile(row - 1, col) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					row--;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				} else {
					canMove = false;
				}
			} else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1;
		while (canMove) { // Direction up left
			if (row != 0 && col != 0) { // checks if out of board bounds
				if (currBoard.getTile(row - 1, col - 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
					row--;
					col--;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				} else {
					canMove = false;
				}
			} else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1;
		while (canMove) { // Direction left
			if (col != 0) { // checks if out of board bounds
				if (currBoard.getTile(row, col - 1) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					col--;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				} else {
					canMove = false;
				}
			} else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1;
		while (canMove) { // Direction down left
			if (row != 9 && col != 0) { // checks if out of board bounds
				if (currBoard.getTile(row + 1, col - 1) == EMPTY) {
					row++;
					col--;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				} else {
					canMove = false;
				}
			} else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1;
		while (canMove) { // Direction down
			if (row != 9) { // checks if out of board bounds
				if (currBoard.getTile(row + 1, col) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					row++;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				} else {
					canMove = false;
				}
			} else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1;
		while (canMove) { // Direction down right
			if (row != 9 && col != 9) { // checks if out of board bounds
				if (currBoard.getTile(row + 1, col + 1) == EMPTY) { // if in bounds checks up right tile to check if can
																	// move (empty)
					row++;
					col++;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				} else {
					canMove = false;
				}
			} else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1;
		while (canMove) { // Direction right
			if (col != 9) { // checks if out of board bounds
				if (currBoard.getTile(row, col + 1) == EMPTY) { // if in bounds checks up right tile to check if can
																// move (empty)
					col++;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				} else {
					canMove = false;
				}
			} else {
				canMove = false;
			}
		}
		return moves;
	}
}
