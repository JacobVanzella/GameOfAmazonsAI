package amazonsChess;

import java.util.List;

public class AmazonsChess {
	public static void main(String[] args) {

		Board board = new Board();
		System.out.println(board.toString());

		int playerWhite = 1;
		int playerBlack = 2;
		RecursiveAI white = new RecursiveAI();
		RecursiveAI black = new RecursiveAI();
		int[][] whiteBoard = new int[10][10];
		int[][] blackBoard = new int[10][10];

		while (board.hasWinner() == 0) {
			List<Integer> bestPlay = white.getPlay(white, playerWhite);

			if (bestPlay != null) {
				board.moveQueen(bestPlay.get(0), bestPlay.get(1), bestPlay.get(2), bestPlay.get(3), playerWhite);
				board.throwSpear(bestPlay.get(4), bestPlay.get(5));
			} else {
				break;
			}

			white.makeBestPlay(white, playerWhite);
			whiteBoard = white.getBoard();
			black.updateBoard(whiteBoard);

			System.out.println(white.toString());
			System.out.println("Black to play\n");

			bestPlay = black.getPlay(black, playerBlack);
			if (bestPlay != null) {
				board.moveQueen(bestPlay.get(0), bestPlay.get(1), bestPlay.get(2), bestPlay.get(3), playerBlack);
				board.throwSpear(bestPlay.get(4), bestPlay.get(5));
			} else {
				break;
			}

			black.makeBestPlay(black, playerBlack);
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
