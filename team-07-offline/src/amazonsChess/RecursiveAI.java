package amazonsChess;

public class RecursiveAI extends Board{

	RecursiveAI parent = null;
	
	RecursiveAI(){
		super(); // calls Board() to create a board for the AI
	}
	
	RecursiveAI( RecursiveAI parent){
		super(parent);
		this.parent = parent;
	}
	
	protected int scoreMove(int charIndex, int player) {
		RecursiveAI testBoard = new RecursiveAI(this); // create copy of board
		testBoard.setTileFromIndex(charIndex, player); // make move in question
		
		/* Implement way to see if already won
		int result = testBoard.checkWin();
		*/
		// Else Recurse
		int scores[] = testBoard.scoreMoves(testBoard, (player == 1) ? 2 : 1 );
		// Implement
		
		return -1;
	}
	
	public int[] scoreMoves( RecursiveAI curBoard, int player) {
		int scores[] = new int[1];
		
		return scores;
	}
	
}
