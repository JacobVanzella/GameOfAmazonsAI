package amazonsChess;

import java.util.ArrayList;
import java.util.List;

// TODO Consider creating a Move class to simplify code
// Constructors
// public class Move(){}
// public class Move(row, col){}
// public class Move(queenCurrRow, queenCurrCol, queenNextRow, queenNextCol)
// public class Move(queenCurrRow, queenCurrCol, queenNextRow, queenNextCol, spearRow, spearCol){}

public class RecursiveAI extends Board {

	RecursiveAI parent = null;

	public RecursiveAI() {
		super(); // calls Board() to create a board for the AI
	}

	public RecursiveAI(int[][] board) {
		super(board);
		//this.board = board;
	}

	public RecursiveAI(RecursiveAI parent) {
		super();
		this.parent = parent;
	}

	public int scoreMove(int[][] board, int[] move, int player) {
		int[][] copyBoard = new int[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				copyBoard[j][i] = board[j][i];
			}
		}
		RecursiveAI testMoveAI = new RecursiveAI(copyBoard); // create copy of board
		testMoveAI.moveQueen(move[0], move[1], move[2], move[3], player);

		int opponent = (player == 1) ? 2 : 1;
		int playerMoves = getMovesArray(testMoveAI, player).size();
		int opponentMoves = getMovesArray(testMoveAI, opponent).size();
		int score = playerMoves - opponentMoves;

		return score;
	}
/*
	public List<List<Integer>> getMoves(RecursiveAI currBoard, int player) {
		// The nested List has form (prevRow, prevCol, nextRow, nextCol) for available
		// moves
		List<List<Integer>> moves = new ArrayList<List<Integer>>(); // will store moves in 2D list
		List<List<Integer>> queenPos = new ArrayList<List<Integer>>();

		// System.out.println(currBoard.toString());

		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (currBoard.getTile(row, col) == player) {
					// System.out.println("Row: " + row + " Col: " + col);
					ArrayList<Integer> queenN = new ArrayList<Integer>();
					queenN.add(row);
					queenN.add(col);
					queenPos.add(queenN);
				}
			}
		}
		// Find possible moves for each queen
		for (List<Integer> queenCordinates : queenPos) {
			int currRow = queenCordinates.get(0);
			int currCol = queenCordinates.get(1);
			int nextRow = currRow;
			int nextCol = currCol;
			int dist = 0;
			boolean canMove = true;
			boolean spearCanMove = true;
			int spearRow = -1;
			int spearCol = -1;
			int spearDist = 0;

			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					// System.out.println(currBoard.toString());
					canMove = true;
					dist = 1;
					try {
						while (canMove) {
							nextRow = currRow + i * dist;
							nextCol = currCol + j * dist;

							if (currBoard.getTile(nextRow, nextCol) == EMPTY) {
								// System.out.println(currRow + " " + currCol + " " + nextRow + " " + nextCol);
								int[][] currBoardArray = new int[10][10];
								for (int ii = 0; ii < 10; ii++) {
									for (int jj = 0; jj < 10; jj++) {
										currBoardArray[jj][ii] = currBoard.getBoard()[jj][ii];
									}
								}
								RecursiveAI testboard = new RecursiveAI(currBoardArray);
								testboard.moveQueen(currRow, currCol, nextRow, nextCol, player);

								for (int k = -1; k < 2; k++) {
									for (int r = -1; r < 2; r++) {
										spearCanMove = true;
										spearDist = 1;
										// spearRow = nextRow;
										// spearCol = nextCol;
										try {
											while (spearCanMove) {
												spearRow = nextRow + k * spearDist;
												spearCol = nextCol + r * spearDist;
												// System.out.println(currRow + " " + currCol + " " + nextRow + " " +
												// nextCol);
												// System.out.println(spearRow + " " + spearCol);
												if (testboard.getTile(spearRow, spearCol) == EMPTY) {
													// System.out.println(testboard.toString());
													ArrayList<Integer> move = new ArrayList<Integer>();
													move.add(currRow);
													move.add(currCol);
													move.add(nextRow);
													move.add(nextCol);
													move.add(spearRow);
													move.add(spearCol);
													//System.out.println(move);
													moves.add(move);
													spearDist++;
												} else {
													spearCanMove = false;
												}
											}
										} catch (Exception e) {
											spearCanMove = false;
										}
									}
								}
								dist++;
							} else {
								canMove = false;
							}
						}
					} catch (Exception e) {
						canMove = false;
					}
				}
			}
		}
		return moves;
	}
	*/

	public List<int[]> getMovesArray(RecursiveAI currBoard, int player) {
		// The nested List has form (prevRow, prevCol, nextRow, nextCol) for available
		// moves
		List<int[]> moves = new ArrayList<int[]>(); // will store array of moves in a list

		// Queens are implemented as a list since the opponent can delete queens when
		// they play invalid moves (will break array)
		List<List<Integer>> queenPos = new ArrayList<List<Integer>>();

		//System.out.println(currBoard.toString()); // for debugging

		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (currBoard.getTile(row, col) == player) {
					// System.out.println("Row: " + row + " Col: " + col); // for debugging
					ArrayList<Integer> queenN = new ArrayList<Integer>();
					queenN.add(row);
					queenN.add(col);
					queenPos.add(queenN);
				}
			}
		}
		// Find possible moves for each queen
		for (List<Integer> queenCordinates : queenPos) {
			int currRow = queenCordinates.get(0);
			int currCol = queenCordinates.get(1);
			int nextRow = currRow;
			int nextCol = currCol;
			int dist = 0;
			boolean canMove = true;
			boolean spearCanMove = true;
			int spearRow = -1;
			int spearCol = -1;
			int spearDist = 0;

			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					// .println(currBoard.toString());
					canMove = true;
					dist = 1;
					try {
						while (canMove) {
							nextRow = currRow + i * dist;
							nextCol = currCol + j * dist;

							if (currBoard.getTile(nextRow, nextCol) == EMPTY) {
								// System.out.println(currRow + " " + currCol + " " + nextRow + " " + nextCol);
								int[][] currBoardArray = new int[10][10];
								for (int ii = 0; ii < 10; ii++) {
									for (int jj = 0; jj < 10; jj++) {
										currBoardArray[jj][ii] = currBoard.getBoard()[jj][ii];
									}
								}
								RecursiveAI testboard = new RecursiveAI(currBoardArray);
								testboard.moveQueen(currRow, currCol, nextRow, nextCol, player);

								for (int k = -1; k < 2; k++) {
									for (int r = -1; r < 2; r++) {
										spearCanMove = true;
										spearDist = 1;
										// spearRow = nextRow;
										// spearCol = nextCol;
										try {
											while (spearCanMove) {
												spearRow = nextRow + k * spearDist;
												spearCol = nextCol + r * spearDist;
												// .println(currRow + " " + currCol + " " + nextRow + " " +
												// nextCol);
												// .println(spearRow + " " + spearCol);
												if (testboard.getTile(spearRow, spearCol) == EMPTY) {
													// .println(testboard.toString());
													int[] move = new int[] { currRow, currCol, nextRow, nextCol,
															spearRow, spearCol };
													moves.add(move);
													spearDist++;
												} else {
													spearCanMove = false;
												}
											}
										} catch (Exception e) {
											spearCanMove = false;
										}
									}
								}
								dist++;
							} else {
								canMove = false;
							}
						}
					} catch (Exception e) {
						canMove = false;
					}
				}
			}
		}
		return moves;
	}

	public boolean wasValidMove(RecursiveAI currBoard, int opponent, int[] move) {
		List<int[]> validMoves = currBoard.getMovesArray(currBoard, opponent);
		for( int[] moveN : validMoves) {
			System.out.print("[");
			for( int i = 0; i < moveN.length; i++) {
				System.out.print(moveN[i] + " ");
			}
			System.out.println("]");
		}
		int prevRowAdjusted = move[0] - 1;
		int prevColAdjusted = move[1] - 1;
		int nextRowAdjusted = move[2] - 1;
		int nextColAdjusted = move[3] - 1;
		int spearRowAdjusted = move[4] - 1;
		int spearColAdjusted = move[5] - 1;
		int[] adjMove = {prevRowAdjusted, prevColAdjusted, nextRowAdjusted, nextColAdjusted, spearRowAdjusted, spearColAdjusted};
		for( int i = 0; i < adjMove.length; i++) {
			System.out.print(adjMove[i] + " ");
		}
		if (validMoves.contains(adjMove) == true) {
			return true;
		} else {
			return false;
		}
	}
}