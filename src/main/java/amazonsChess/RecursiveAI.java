package amazonsChess;

import java.util.ArrayList;
import java.util.List;

public class RecursiveAI extends Board {

	RecursiveAI parent = null;

	public RecursiveAI() {
		super(); // calls Board() to create a board for the AI
	}
	
	public RecursiveAI(int[][] board) {
		super();
		this.board = board;
	}

	public RecursiveAI(RecursiveAI parent) {
		super(parent);
		this.parent = parent;
	}
	
	
	////////////////////////////////////////////////////////////
	// Start
	// fetchPlays seems good to have as a public class, but getPlays feels
	// redundant, is there a way we can use the existing functions to achieve
	// the same result??
	public List<List<Integer>> fetchPlays( RecursiveAI currBoard, int player){
		List<List<Integer>> moves = currBoard.getMoves(currBoard, player);
		List<List<Integer>> plays = currBoard.getPlays(currBoard, moves, player);
		
		return plays;
	}
	
	protected List<List<Integer>> getPlays(RecursiveAI currBoard, List<List<Integer>> moves, int player){
		List<List<Integer>> plays = new ArrayList<List<Integer>>();
		for( List<Integer> move : moves) {
			RecursiveAI testboard = currBoard;
			testboard.moveQueen(move.get(0), move.get(1), move.get(2), move.get(3), player);
			List<List<Integer>> spearThrow = testboard.getSpearMoves(move.get(2), move.get(3), testboard, player);
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
	// End
	////////////////////////////////////////////////////////////

	protected void makeBestPlay(RecursiveAI ai, int player) {
		List<Integer> bestPlay = getPlay(ai, player);
		if (bestPlay != null) {
			int prevRow = bestPlay.get(0);
			int prevCol = bestPlay.get(1);
			int bestRow = bestPlay.get(2);
			int bestCol = bestPlay.get(3);
			int spearRow = bestPlay.get(4);
			int spearCol = bestPlay.get(5);
			ai.moveQueen(prevRow, prevCol, bestRow, bestCol, player);
			ai.throwSpear(spearRow, spearCol);
		}
	}

	protected List<Integer> getPlay(RecursiveAI ai, int player) {
		List<Integer> bestMove = ai.scoreMoves(ai, player);
		if (bestMove.size() != 0) {
			List<Integer> bestThrow = ai.spearThrow(ai, bestMove, player);
			List<Integer> bestPlay = bestMove;
			bestPlay.addAll(bestThrow);
			return bestPlay;
		} else {
			return null;
		}
	}

	protected List<Integer> spearThrow(RecursiveAI currBoard, List<Integer> bestMove, int player) {
		RecursiveAI testBoard = new RecursiveAI(this); // create copy of board
		testBoard.moveQueen(bestMove.get(0), bestMove.get(1), bestMove.get(2), bestMove.get(3), player);
		List<List<Integer>> moves = testBoard.getSpearMoves(bestMove.get(2), bestMove.get(3), testBoard, player);
		int bestThrowScore = Integer.MIN_VALUE;
		int bestRow = -1;
		int bestCol = -1;
		int row = -1;
		int col = -1;
		
		for (List<Integer> move : moves) {
			row = move.get(2);
			col = move.get(3);
			RecursiveAI trialBoard = new RecursiveAI(this);
			trialBoard.throwSpear(row, col);
			int opponent = (player == 1) ? 2 : 1;
			int playerMoves = getMoves(trialBoard, player).size();
			int opponentMoves = getMoves(trialBoard, opponent).size();
			int score = playerMoves - opponentMoves;
			
			if (score > bestThrowScore) {
				bestThrowScore = score;
				bestRow = row;
				bestCol = col;
			}
		}
		
		if (bestThrowScore == Integer.MIN_VALUE) {
			bestRow = bestMove.get(0);
			bestCol = bestMove.get(1);
		}
		
		List<Integer> bestThrow = new ArrayList<Integer>();
		bestThrow.add(bestRow);
		bestThrow.add(bestCol);
		return bestThrow;
	}

	protected int scoreMove(List<Integer> move, int player) {
		RecursiveAI testBoard = new RecursiveAI(this); // create copy of board
		testBoard.moveQueen(move.get(0), move.get(1), move.get(2), move.get(3), player);

		int opponent = (player == 1) ? 2 : 1;
		int playerMoves = getMoves(testBoard, player).size();
		int opponentMoves = getMoves(testBoard, opponent).size();
		int score = playerMoves - opponentMoves;

		// Else Recurse
		// int scores[] = testBoard.scoreMoves(testBoard, (player == 1) ? 2 : 1 );
		// Implement

		return score;
	}

	public List<Integer> scoreMoves(RecursiveAI currBoard, int player) {
		List<List<Integer>> moves = currBoard.getMoves(currBoard, player);
		int bestMoveScore = Integer.MIN_VALUE;
		List<Integer> bestMove = new ArrayList<Integer>();
		for (List<Integer> move : moves) {
			int moveScore = currBoard.scoreMove(move, player);
			if (moveScore > bestMoveScore) {
				bestMove = move;
				bestMoveScore = moveScore;
			}
		}
		return bestMove;
	}

	protected List<List<Integer>> getMoves(RecursiveAI currBoard, int player) {

		// The nested List has form (prevRow, prevCol, nextRow, nextCol) for available
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

		// Find possible moves for each queen
		for (List<Integer> queenCordinates : queenPos) {
			int currRow = queenCordinates.get(0);
			int currCol = queenCordinates.get(1);
			int nextRow = currRow, nextCol = currCol, dist = 0;
			boolean canMove = true;

			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					canMove = true;
					dist = 1;
					try {
						while (canMove) {
							nextRow = currRow + i * dist;
							nextCol = currCol + j * dist;
							
							if (currBoard.getTile(nextRow, nextCol) == EMPTY) {
								ArrayList<Integer> move = new ArrayList<Integer>();
								move.add(currRow);
								move.add(currCol);
								move.add(nextRow);
								move.add(nextCol);
								moves.add(move);
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

	protected List<List<Integer>> getSpearMoves(int currRow, int currCol, RecursiveAI currBoard, int player) {

		// The nested List has form (prevRow, prevCol, nextRow, nextCol) for avaliable
		// moves
		List<List<Integer>> moves = new ArrayList<List<Integer>>(); // will store moves in 2D list

		// Find possible moves for each queen
		boolean canMove = true;
		int nextRow = currRow, nextCol = currCol, dist = 0;
		
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				canMove = true;
				dist = 1;
				try {
					while (canMove) {
						nextRow = currRow + i * dist;
						nextCol = currCol + j * dist;
						
						if (currBoard.getTile(nextRow, nextCol) == EMPTY) {
							ArrayList<Integer> move = new ArrayList<Integer>();
							move.add(currRow);
							move.add(currCol);
							move.add(nextRow);
							move.add(nextCol);
							moves.add(move);
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
		return moves;
	}
}
