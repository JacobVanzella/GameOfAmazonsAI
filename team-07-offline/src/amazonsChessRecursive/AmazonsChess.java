package amazonsChessRecursive;

import java.util.List;

public class AmazonsChess {
	public static void main(String[] args) {

		Board board = new Board();
		System.out.println(board.toString());

		int playerWhite = 1;
		int playerBlack = 2;
		RecursiveAI white = new RecursiveAI();
		RecursiveAI black = new RecursiveAI();
		int[] whiteBoard = new int[100];
		int[] blackBoard = new int[100];
		while (board.hasWinner() == 0) {
			List<Integer> bestPlay = white.getPlay(white, playerWhite);
			if( bestPlay != null) {
				System.out.println(bestPlay);
				board.moveQueen(bestPlay.get(0), bestPlay.get(1), bestPlay.get(2), bestPlay.get(3), playerWhite);
				board.throwSpear(bestPlay.get(4), bestPlay.get(5));
			}else {
				System.out.println("White Lost");
				break;
			}
			white.makeBestPlay(white, playerWhite);
			whiteBoard = white.getBoard();
			black.updateBoard(whiteBoard);
			System.out.println(white.toString());
			System.out.println("Black to play");
			//System.out.println(black.getMoves(black, playerBlack));
			//System.out.println(black.scoreMoves(black, playerBlack));
			bestPlay = black.getPlay(black, playerBlack);
			if (bestPlay != null) {
				System.out.println(bestPlay);
				board.moveQueen(bestPlay.get(0), bestPlay.get(1), bestPlay.get(2), bestPlay.get(3), playerBlack);
				board.throwSpear(bestPlay.get(4), bestPlay.get(5));
			}else {
				System.out.println("Black lost");
				break;
			}
			black.makeBestPlay(black, playerBlack);
			blackBoard = black.getBoard();
			white.updateBoard(blackBoard);
			System.out.println(black.toString());
			System.out.println("White to play");
		}
		if( board.hasWinner() == 1) {
			System.out.println("White wins");
		}else if( board.hasWinner() == 2) {
			System.out.println("Black wins");
		}
		/*
		 * while( !white.hasWon(white, playerWhite)) {
		 * 
		 * }
		 */

	}
}
