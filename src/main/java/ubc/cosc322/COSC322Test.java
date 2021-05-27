package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import amazonsChess.RecursiveAI;
import amazonsChessRecursive.Node;
import amazonsChessRecursive.Tree;
import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

// An example illustrating how to implement a GamePlayer
// @author Yong Gao (yong.gao@ubc.ca) Jan 5, 2021

public class COSC322Test extends GamePlayer {

	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;
	private ArrayList<Integer> gameState = null; // Stores game state locally

	private String userName = null;
	private String passwd = null;
	private int player = 0;

	// The main method
	// @param args for name and passwd (current, any string would work)
	public static void main(String[] args) {
		COSC322Test player = new COSC322Test(args[0], args[1]);
		
		RecursiveAI testBot = new RecursiveAI();
		int[][] prevMoves = new int[1][6];
		int[] currentMove = new int[6];
		System.out.println(testBot.toString());
		
		Node root = new Node(prevMoves, currentMove, null, -1);
		Tree tree = new Tree(root, testBot, 1);
		//System.out.println(tree.foundNodes);
		System.out.println("HERE"+tree.alphaBeta(root, Integer.MIN_VALUE, Integer.MAX_VALUE));
		
		/*
		 * for( int[] move : moves) { System.out.print("["); for( int i = 0; i <
		 * move.length; i ++) { System.out.print(move[i] + ", " ); }
		 * System.out.println("]"); }
		 */

		if (player.getGameGUI() == null) {
			player.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player.Go();
				}
			});
		}
	}

	// Any name and passwd
	// @param userName
	// @param passwd
	public COSC322Test(String userName, String passwd) {
		this.userName = userName;
		this.passwd = passwd;
		this.gamegui = new BaseGameGUI(this);
	}

	@Override
	public void onLogin() {
		System.out.println(
				"Congratualations!!! " + "I am called because the server indicated that the login is successfully");

		List<Room> rooms = this.gameClient.getRoomList();
		for (Room room : rooms) {
			System.out.println(room.toString());
		}
		System.out.println();

		userName = gameClient.getUserName();
		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		// This method will be called by the GameClient when it receives a game-related
		// message from the server.
		ArrayList<Integer> gameState, queenPosCurr, queenPosNext, arrowPos;
		String playerBlack, playerWhite;
		gameState = this.gameState;
		System.out.println("Message Type: " + messageType);
		
		
		
		// Catches failure to handle message and returns false with error printed to
		// console
		try {
			// Handle console output based on message type
			switch (messageType) {
			case GameMessage.GAME_ACTION_MOVE:
				queenPosCurr = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
				queenPosNext = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT);
				arrowPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
				System.out.println("Moved Queen: " + queenPosCurr.toString() + " -> " + queenPosNext.toString()
						+ " (Arrow: " + arrowPos + ")");

				// Update GUI board with new state after move
				this.gamegui.updateGameState(queenPosCurr, queenPosNext, arrowPos);
				
				//Check to see if the opponent made a valid move
				// Will look at all possible moves for opponent and make sure it contains their move.
				ArrayList<Integer> testBoard = new ArrayList<Integer>();
				int[][] arrayTestBoard = new int[10][10];
				for (int i = 0; i < gameState.size(); i++) {
					if (i % 11 != 0 && i > 11) {
						testBoard.add(gameState.get(i));
					}
				}

				int k = 0;
				for (int i = 0; i < testBoard.size(); i++) {
					if (i % 10 == 0 && i != 0)
						k ++;
					arrayTestBoard[k][i % 10] = testBoard.get(i);
				}
				RecursiveAI testOpponentMove = new RecursiveAI(arrayTestBoard);
				
				int[] opponentMove = new int[] {queenPosCurr.get(0), queenPosCurr.get(1), queenPosNext.get(0), queenPosNext.get(1), arrowPos.get(0), arrowPos.get(1)};
				if( testOpponentMove.wasValidMove(testOpponentMove, (player == 1) ? 2 : 1, opponentMove) == false) {
					System.out.println("Opponent made an invalid move");
					this.gameClient.sendTextMessage("Opponent made an invalid move");
				}
				// End valid move checker
				
				// Update local board with most recent move
				gameState.set(queenPosCurr.get(0) * 11 + queenPosCurr.get(1), 0);
				gameState.set(queenPosNext.get(0) * 11 + queenPosNext.get(1), (player == 1) ? 2 : 1);
				gameState.set(arrowPos.get(0) * 11 + arrowPos.get(1), 3);
				// Creates local game board that has correct size (index 0->9 x 0->9, i.e 10x10
				// board)
				ArrayList<Integer> gameBoard = new ArrayList<Integer>();
				int[][] arrayBoard = new int[10][10];
				for (int i = 0; i < gameState.size(); i++) {
					if (i % 11 != 0 && i > 11) {
						gameBoard.add(gameState.get(i));
					}
				}

				int j = 0;
				for (int i = 0; i < gameBoard.size(); i++) {
					if (i % 10 == 0 && i != 0)
						j ++;
					arrayBoard[j][i % 10] = gameBoard.get(i);
				}
				
				
				RecursiveAI ai = new RecursiveAI(arrayBoard);
				ai.printBoard();
				System.out.println("PLAYER: " + player);
				List<List<Integer>> moves = ai.getMoves(ai, player);
				
				System.out.println("Currn List: "+moves);
				if (moves.size() != 0) {
					int randomIndex = (int) (Math.random() * moves.size());
					List<Integer> randomMove = moves.get(randomIndex);
					System.out.println(randomMove);
					int prevRow = randomMove.get(0);
					int prevCol = randomMove.get(1);
					int nextRow = randomMove.get(2);
					int nextCol = randomMove.get(3);
					int spearRow = randomMove.get(4);
					int spearCol = randomMove.get(5);
					int prevRowAdjusted = prevRow + 1;
					int prevColAdjusted = prevCol + 1;
					int nextRowAdjusted = nextRow + 1;
					int nextColAdjusted = nextCol + 1;
					int spearRowAdjusted = spearRow + 1;
					int spearColAdjusted = spearCol + 1;
					ArrayList<Integer> queenToMove = new ArrayList<Integer>();
					queenToMove.add(prevRowAdjusted);
					queenToMove.add(prevColAdjusted);
					ArrayList<Integer> queenGoesTo = new ArrayList<Integer>();
					queenGoesTo.add(nextRowAdjusted);
					queenGoesTo.add(nextColAdjusted);
					ArrayList<Integer> spearGoesTo = new ArrayList<Integer>();
					spearGoesTo.add(spearRowAdjusted);
					spearGoesTo.add(spearColAdjusted);
					System.out.println(queenToMove + " " + queenGoesTo + " " + spearGoesTo);
					this.gameClient.sendMoveMessage(queenToMove, queenGoesTo, spearGoesTo);
					this.gamegui.updateGameState(queenToMove, queenGoesTo, spearGoesTo);
					gameState.set(queenToMove.get(0) * 11 + queenToMove.get(1), 0);
					gameState.set(queenGoesTo.get(0) * 11 + queenGoesTo.get(1), player);
					gameState.set(spearGoesTo.get(0) * 11 + spearGoesTo.get(1), 3);
					this.gameState = gameState;
				} else {
					//If there are no moves avaliable, move to a random spot on the board and throw a spear
					//to a random spot, hope the opponent is not checking valid moves.
					int dummyQueenRow = 1;
					int dummyQueenCol = 1;
					for ( int i = 0 ; i < 10; i ++) {
						for( int ii = 0; ii < 10; ii ++) {
							if(ai.getTile(i, ii) == player) {
								dummyQueenRow = i;
								dummyQueenCol = ii;
							}
						}
					}
					int dummyToRow = (int) (Math.random() * 10 + 1);
					int dummyToCol = (int) (Math.random() * 10 + 1);
					int dummySpearRow = (int) (Math.random() * 10 + 1);
					int dummySpearCol = (int) (Math.random() * 10 + 1);
					ArrayList<Integer> dummyQueenLoc = new ArrayList<Integer>();
					dummyQueenLoc.add(dummyQueenRow);
					dummyQueenLoc.add(dummyQueenCol);
					ArrayList<Integer> dummyQueenTo = new ArrayList<Integer>();
					dummyQueenTo.add(dummyToRow);
					dummyQueenTo.add(dummyToCol);
					ArrayList<Integer> dummySpear = new ArrayList<Integer>();
					dummySpear.add(dummySpearRow);
					dummySpear.add(dummySpearCol);
					System.out.println(dummyQueenLoc + " " + dummyQueenTo + " " + dummySpear);
					this.gameClient.sendMoveMessage(dummyQueenLoc, dummyQueenTo, dummySpear);
					this.gamegui.updateGameState(dummyQueenLoc, dummyQueenTo, dummySpear);
					gameState.set(dummyQueenRow * 11 + dummyQueenCol, 0);
					gameState.set(dummyToRow * 11 + dummyToCol, player);
					gameState.set(dummySpearRow * 11 + dummySpearCol, 3);
					this.gameState = gameState;
				}
				break;

			case GameMessage.GAME_ACTION_START:
				playerBlack = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
				playerWhite = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
				player = (userName.equals(playerBlack)) ? 1 : 2;
				System.out.println("PLAYER IS: " + player + " " + playerBlack + " " + userName);
				//gameState = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);
				
				System.out.println("\nGame Started!!\n" + playerBlack + "(B) vs. " + playerWhite + "(W)");
				if( player == 1) {
					int firstQueenPrevRow = 10;
					int firstQueenPrevCol = 4;
					int firstQueenNextRow = 4;
					int firstQueenNextCol = 4;
					int firstSpearRow = 4;
					int firstSpearCol = 7;
										
					ArrayList<Integer> queenFirstMove = new ArrayList<Integer>();
					queenFirstMove.add(firstQueenPrevRow);
					queenFirstMove.add(firstQueenPrevCol);
					ArrayList<Integer> queenMovesTo = new ArrayList<Integer>();
					queenMovesTo.add(firstQueenNextRow);
					queenMovesTo.add(firstQueenNextCol);
					ArrayList<Integer> spearLands = new ArrayList<Integer>();
					spearLands.add(firstSpearRow);
					spearLands.add(firstSpearCol);
					this.gameClient.sendMoveMessage(queenFirstMove, queenMovesTo, spearLands);
					this.gamegui.updateGameState(queenFirstMove, queenMovesTo, spearLands);
					gameState.set(firstQueenPrevRow * 11 + firstQueenPrevCol, 0);
					gameState.set(firstQueenNextRow * 11 + firstQueenNextCol, player);
					gameState.set(firstSpearRow * 11 + firstSpearCol, 3);
					this.gameState = gameState;
				}
				/*
				 * System.out.print("Board State: "); for (int i = 11; i < gameState.size();
				 * i++) { if (i % 11 == 0) System.out.println(); else
				 * System.out.print(gameState.get(i)); } System.out.println("\n");
				 */ 
				break;

			case GameMessage.GAME_STATE_BOARD:
				gameState = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);

				System.out.print("\nBoard State:");
				for (int i = 11; i < gameState.size(); i++) {
					if (i % 11 == 0)
						System.out.println();
					else
						System.out.print(gameState.get(i));
				}
				System.out.println("\n");

				// Set GUI board with state
				this.gamegui.setGameState(gameState);
				this.gameState = gameState; // Store board state locally
				break;
			}
		} catch (Exception e) {
			System.err.println(e.toString());
			System.err.println("\nERROR: Message Handling Failed\nmessageType: " + messageType + "\nmsgDetails: "
					+ msgDetails.toString());
			throw(e);
			//return false;
		}
		return true;
	}

	@Override
	public String userName() {
		return userName;
	}

	@Override
	public GameClient getGameClient() {
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		return this.gamegui;
	}

	@Override
	public void connect() {
		gameClient = new GameClient(userName, passwd, this);
	}
}// end of class