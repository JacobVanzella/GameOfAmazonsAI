package amazonsChess;

import java.util.ArrayList;
import java.util.List;

public class RecursiveAI extends Board{

	RecursiveAI parent = null;
	
	RecursiveAI(){
		super(); // calls Board() to create a board for the AI
	}
	
	RecursiveAI( RecursiveAI parent){
		super(parent);
		this.parent = parent;
	}
	
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
	
	protected List<Integer> getPlay( RecursiveAI ai, int player){
		List<Integer> bestMove = ai.scoreMoves(ai, player);
		if( bestMove.size() != 0) {
			List<Integer> bestThrow = ai.spearThrow(ai, bestMove, player);
			List<Integer> bestPlay = bestMove;
			bestPlay.addAll(bestThrow);
			return bestPlay;
		}else {
			return null;
		}
	}
	
	protected List<Integer> spearThrow( RecursiveAI currBoard, List<Integer> bestMove, int player){
		RecursiveAI testBoard = new RecursiveAI(this); // create copy of board
		testBoard.moveQueen(bestMove.get(0), bestMove.get(1), bestMove.get(2), bestMove.get(3), player);
		List<List<Integer>> moves = currBoard.getMoves(bestMove.get(2), bestMove.get(3), currBoard, player);
		int bestThrowScore = -999999;
		int bestRow = -1;
		int bestCol = -1;
		int row = -1;
		int col = -1;
		for( List<Integer> move : moves) {
			row = move.get(2);
			col = move.get(3);
			RecursiveAI trialBoard = new RecursiveAI(this);
			trialBoard.throwSpear(row, col);
			int opponent = (player == 1) ? 2 : 1;
			int playerMoves = getMoves(trialBoard, player).size();
			int opponentMoves = getMoves(trialBoard, opponent).size();
			int score = playerMoves - opponentMoves;
			if( score > bestThrowScore) {
				bestThrowScore = score;
				bestRow = row;
				bestCol = col;
			}
		}
		if( bestThrowScore == -999999) {
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
		//int scores[] = testBoard.scoreMoves(testBoard, (player == 1) ? 2 : 1 );
		// Implement
		
		return score;
	}
	
	public List<Integer> scoreMoves( RecursiveAI currBoard, int player) {
		List<List<Integer>> moves = currBoard.getMoves(currBoard, player);
		int bestMoveScore = -99999999;
		List<Integer> bestMove = new ArrayList<Integer>();
		for( List<Integer> move : moves) {
			int moveScore = currBoard.scoreMove(move, player);
			if( moveScore > bestMoveScore) {
				bestMove = move;
				bestMoveScore = moveScore;
			}
		}
		return bestMove;
	}
	protected List<List<Integer>> getMoves(RecursiveAI currBoard, int player) {
		
		// The nested List has form (prevRow, prevCol, nextRow, nextCol) for avaliable moves
		List<List<Integer>> moves = new ArrayList<List<Integer>>(); // will store moves in 2D list
		
		List<List<Integer>> queenPos = new ArrayList<List<Integer>>();
		for( int row = 0; row < 10; row ++) {
			for( int col = 0; col < 10; col ++) {
				if( currBoard.getTile(row, col) == player) {
					ArrayList<Integer> queenN = new ArrayList<Integer>();
					queenN.add(row);
					queenN.add(col);
					queenPos.add(queenN);
				}
			}	
		}
		// Find possible moves for each queen, will sweep from top right CCW
		for( List<Integer> queenCordinates : queenPos) {
		int currRow1 = queenCordinates.get(0);
		int currCol1 = queenCordinates.get(1);
		boolean canMove = true;
		int row = currRow1, col = currCol1;
		while( canMove) { //Direction up right
			if( row != 0 && col != 9 ) { // checks if out of board bounds
				if( currBoard.getTile(row-1, col+1) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
					row --;
					col ++;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				}else {
					canMove = false;
				}
			}else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1; 
		while( canMove) { //Direction up
			if( row != 0 ) { // checks if out of board bounds
				if( currBoard.getTile(row-1, col) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
					row --;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				}else {
					canMove = false;
				}
			}else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1; 
		while( canMove) { //Direction up left
			if( row != 0 && col != 0) { // checks if out of board bounds
				if( currBoard.getTile(row-1, col-1) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
					row --;
					col --;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				}else {
					canMove = false;
				}
			}else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1; 
		while( canMove) { //Direction left
			if( col != 0 ) { // checks if out of board bounds
				if( currBoard.getTile(row, col-1) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
					col --;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				}else {
					canMove = false;
				}
			}else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1;
		while( canMove) { //Direction down left
			if( row != 9 && col != 0 ) { // checks if out of board bounds
				if( currBoard.getTile(row+1, col-1) == EMPTY) {
					row ++;
					col --;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				}else {
					canMove = false;
				}
			}else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1; 
		while( canMove) { //Direction down
			if( row != 9 ) { // checks if out of board bounds
				if( currBoard.getTile(row+1, col) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
					row ++;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				}else {
					canMove = false;
				}
			}else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1; 
		while( canMove) { //Direction down right
			if( row != 9 && col != 9 ) { // checks if out of board bounds
				if( currBoard.getTile(row+1, col+1) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
					row ++;
					col ++;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				}else {
					canMove = false;
				}
			}else {
				canMove = false;
			}
		}
		canMove = true;
		row = currRow1;
		col = currCol1; 
		while( canMove) { //Direction right
			if( col != 9 ) { // checks if out of board bounds
				if( currBoard.getTile(row, col+1) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
					col++;
					ArrayList<Integer> move = new ArrayList<Integer>();
					move.add(currRow1);
					move.add(currCol1);
					move.add(row);
					move.add(col);
					moves.add(move);
				}else {
					canMove = false;
				}
			}else {
				canMove = false;
			}
		}
		}
		return moves;
	}
protected List<List<Integer>> getMoves(int queenRow, int queenCol, RecursiveAI currBoard, int player) {
	
	// The nested List has form (prevRow, prevCol, nextRow, nextCol) for avaliable moves
	List<List<Integer>> moves = new ArrayList<List<Integer>>(); // will store moves in 2D list
	
	
	// Find possible moves for each queen, will sweep from top right CCW
	int currRow1 = queenRow;
	int currCol1 = queenCol;
	boolean canMove = true;
	int row = currRow1, col = currCol1;
	while( canMove) { //Direction up right
		if( row != 0 && col != 9 ) { // checks if out of board bounds
			if( currBoard.getTile(row-1, col+1) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
				row --;
				col ++;
				ArrayList<Integer> move = new ArrayList<Integer>();
				move.add(currRow1);
				move.add(currCol1);
				move.add(row);
				move.add(col);
				moves.add(move);
			}else {
				canMove = false;
			}
		}else {
			canMove = false;
		}
	}
	canMove = true;
	row = currRow1;
	col = currCol1; 
	while( canMove) { //Direction up
		if( row != 0 ) { // checks if out of board bounds
			if( currBoard.getTile(row-1, col) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
				row --;
				ArrayList<Integer> move = new ArrayList<Integer>();
				move.add(currRow1);
				move.add(currCol1);
				move.add(row);
				move.add(col);
				moves.add(move);
			}else {
				canMove = false;
			}
		}else {
			canMove = false;
		}
	}
	canMove = true;
	row = currRow1;
	col = currCol1; 
	while( canMove) { //Direction up left
		if( row != 0 && col != 0) { // checks if out of board bounds
			if( currBoard.getTile(row-1, col-1) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
				row --;
				col --;
				ArrayList<Integer> move = new ArrayList<Integer>();
				move.add(currRow1);
				move.add(currCol1);
				move.add(row);
				move.add(col);
				moves.add(move);
			}else {
				canMove = false;
			}
		}else {
			canMove = false;
		}
	}
	canMove = true;
	row = currRow1;
	col = currCol1; 
	while( canMove) { //Direction left
		if( col != 0 ) { // checks if out of board bounds
			if( currBoard.getTile(row, col-1) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
				col --;
				ArrayList<Integer> move = new ArrayList<Integer>();
				move.add(currRow1);
				move.add(currCol1);
				move.add(row);
				move.add(col);
				moves.add(move);
			}else {
				canMove = false;
			}
		}else {
			canMove = false;
		}
	}
	canMove = true;
	row = currRow1;
	col = currCol1;
	while( canMove) { //Direction down left
		if( row != 9 && col != 0 ) { // checks if out of board bounds
			if( currBoard.getTile(row+1, col-1) == EMPTY) {
				row ++;
				col --;
				ArrayList<Integer> move = new ArrayList<Integer>();
				move.add(currRow1);
				move.add(currCol1);
				move.add(row);
				move.add(col);
				moves.add(move);
			}else {
				canMove = false;
			}
		}else {
			canMove = false;
		}
	}
	canMove = true;
	row = currRow1;
	col = currCol1; 
	while( canMove) { //Direction down
		if( row != 9 ) { // checks if out of board bounds
			if( currBoard.getTile(row+1, col) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
				row ++;
				ArrayList<Integer> move = new ArrayList<Integer>();
				move.add(currRow1);
				move.add(currCol1);
				move.add(row);
				move.add(col);
				moves.add(move);
			}else {
				canMove = false;
			}
		}else {
			canMove = false;
		}
	}
	canMove = true;
	row = currRow1;
	col = currCol1; 
	while( canMove) { //Direction down right
		if( row != 9 && col != 9 ) { // checks if out of board bounds
			if( currBoard.getTile(row+1, col+1) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
				row ++;
				col ++;
				ArrayList<Integer> move = new ArrayList<Integer>();
				move.add(currRow1);
				move.add(currCol1);
				move.add(row);
				move.add(col);
				moves.add(move);
			}else {
				canMove = false;
			}
		}else {
			canMove = false;
		}
	}
	canMove = true;
	row = currRow1;
	col = currCol1; 
	while( canMove) { //Direction right
		if( col != 9 ) { // checks if out of board bounds
			if( currBoard.getTile(row, col+1) == EMPTY) { // if in bounds checks up right tile to check if can move (empty)
				col++;
				ArrayList<Integer> move = new ArrayList<Integer>();
				move.add(currRow1);
				move.add(currCol1);
				move.add(row);
				move.add(col);
				moves.add(move);
			}else {
				canMove = false;
			}
		}else {
			canMove = false;
		}
	}
	return moves;
}
}
