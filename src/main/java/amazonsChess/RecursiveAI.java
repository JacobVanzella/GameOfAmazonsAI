package amazonsChess;

import java.util.ArrayList;
import java.util.List;

public class RecursiveAI extends Board {

	RecursiveAI parent = null;

	public RecursiveAI() {
		super(); // calls Board() to create a board for the AI
	}

	public RecursiveAI(int[][] board) {
		super(board);
	}

	public int scoreMove(int[][] board, int[] move, int player) {
		
		RecursiveAI testMoveAI = new RecursiveAI(board); // create copy of board
		testMoveAI.moveQueen(move[0], move[1], move[2], move[3], player);
		testMoveAI.throwSpear(move[4], move[5]);

		int opponent = (player == 1) ? 2 : 1;
		int playerMoves = getMovesArray(testMoveAI, player).size();
		int opponentMoves = getMovesArray(testMoveAI, opponent).size();
		int score = playerMoves - opponentMoves;

		return score;
	}


	public List<int[]> getMovesArray(RecursiveAI currBoard, int player) {
		// The nested List has form (prevRow, prevCol, nextRow, nextCol) for available
		// moves
		List<int[]> moves = new ArrayList<int[]>(); // will store array of moves in a list

		// Queens are implemented as a list since the opponent can delete queens when
		// they play invalid moves (will break array)
		List<List<Integer>> queenPos = new ArrayList<List<Integer>>();

		// loop to find each queen position for the given player
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
					canMove = true;
					dist = 1;
					try {
						while (canMove) {
							nextRow = currRow + i * dist;
							nextCol = currCol + j * dist;

							if (currBoard.getTile(nextRow, nextCol) == EMPTY) {
								RecursiveAI testboard = new RecursiveAI(currBoard.getBoard());
								testboard.moveQueen(currRow, currCol, nextRow, nextCol, player);

								for (int k = -1; k < 2; k++) {
									for (int r = -1; r < 2; r++) {
										spearCanMove = true;
										spearDist = 1;
										try {
											while (spearCanMove) {
												spearRow = nextRow + k * spearDist;
												spearCol = nextCol + r * spearDist;
												if (testboard.getTile(spearRow, spearCol) == EMPTY) {
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
		int prevRowAdjusted = move[0] - 1;
		int prevColAdjusted = move[1] - 1;
		int nextRowAdjusted = move[2] - 1;
		int nextColAdjusted = move[3] - 1;
		int spearRowAdjusted = move[4] - 1;
		int spearColAdjusted = move[5] - 1;
		int[] adjMove = {prevRowAdjusted, prevColAdjusted, nextRowAdjusted, nextColAdjusted, spearRowAdjusted, spearColAdjusted};
		
		for ( int[] moveN : validMoves) {
			boolean contains = true;
			for( int i = 0; i < move.length; i ++) {
				if( moveN[i] != adjMove[i]) {
					contains = false;
				}
				if( i == move.length - 1 && contains == true) {
					return true;
				}
			}
		}
		
		return false;
	}
}