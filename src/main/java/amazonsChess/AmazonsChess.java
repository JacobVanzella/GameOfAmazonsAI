package amazonsChess;

import java.util.List;

public class AmazonsChess {
	public static void main(String[] args) {

		
		// This is not yet working
		// it will be able to test the AI's offline
		//
		//
		//
		//
		
		Board board = new Board();
		System.out.println(board.toString());

		int playerWhite = 1;
		int playerBlack = 2;
		RecursiveAI white = new RecursiveAI();
		RecursiveAI black = new RecursiveAI();
		int[][] whiteBoard = new int[10][10];
		int[][] blackBoard = new int[10][10];

		while (board.hasWinner() == 0) {

			// White makes move
			
			whiteBoard = white.getBoard();
			black.updateBoard(whiteBoard);

			System.out.println(white.toString());
			System.out.println("Black to play\n");
			
			// Black makes move
			
			blackBoard = black.getBoard();
			white.updateBoard(blackBoard);

			System.out.println(black.toString());
			System.out.println("White to play\n");
		}

		if (board.hasWinner() == 1) {
			System.out.println("White wins");
		} else if (board.hasWinner() == 2) {
			System.out.println("Black wins");
		}
	}
}
