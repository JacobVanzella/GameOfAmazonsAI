package amazonsChessRecursive;

import java.util.ArrayList;
import java.util.List;

import amazonsChess.Board;
import amazonsChess.RecursiveAI;

public class Tree {
	private Node root;
	private int depth;
	private ArrayList<Node> frontier = new ArrayList<Node>();
	public ArrayList<Node> foundNodes = new ArrayList<Node>();
	private int[][] board = new int[10][10];
	int player = 0;
	int opponent = 0;

	public Tree(Node root, RecursiveAI currBoard, int player) {
		this.root = root;
		this.player = player;
		this.opponent = (this.player == 1) ? 2 : 1;

		frontier.add(root);

		// Initializes timer
		Long startTime = System.currentTimeMillis();

		// Stores version of the board locally
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				this.board[j][i] = currBoard.getTile(j, i);
			}
		}

		// To be updated in the while loop
		Long currTime = System.currentTimeMillis();

		// While timer elapsed < 20 seconds (for now), do again. /1e3 comverts from ms
		// to s

		while ((currTime - startTime) / 1000 < 30) {

			expandFrontier();

			// Update time for evaluation in while loop
			currTime = System.currentTimeMillis();
			System.out.println("WORKING:" + (currTime - startTime) / 1000);
		}
		System.out.println("DONE");
	}

	public void getDepth() { // Q: this is checking the depth of the left-most branch, if this is a terminal
								// node immediatly, but the others are not, wont this lead to a "false depth"
		Node node = this.root;
		int depth = 0;
		while (node != null) {
			List<Node> children = node.getChildren();
			if (children.isEmpty())
				break;
			else {
				node = children.get(0);
				depth++;
			}
			this.depth = depth;
		}
	}

	public int getNodeDepth(Node node) {
		if (node.getParentNode() != null) {
			return 1 + getNodeDepth(node.getParentNode());
		} else {
			return 0;
		}
	}

	public void expandFrontier() {
		Node frontierNode = this.frontier.get(0);
		List<Node> newFoundNodes = expandNode(frontierNode);

		this.foundNodes.add(frontierNode); // if errors, check these children
		for (Node node : newFoundNodes) {
			this.frontier.add(node);
		}
		this.frontier.remove(0);

	}

	public List<Node> expandNode(Node node) {
		// the new nodes that are going to be added to the frontier list.
		List<Node> discoveredNodes = new ArrayList<Node>();

		int[][] parentMoveList = node.getMoveList();
		// if depth is odd it is there move, else it is ours
		int currentTurn = (this.getNodeDepth(node) % 2 == 1) ? opponent : player;

		// new AI that will be used to execute move list, then find new moves
		RecursiveAI testBoard = new RecursiveAI(this.board);

		// makes moves to get to nodes board state (since we are storing moves to get
		// there not the board itself)
		if (parentMoveList != null) {
			for (int[] move : parentMoveList) {
				System.out.println(
						move[0] + "," + move[1] + "->" + move[2] + "," + move[3] + " Spear:" + move[4] + "," + move[5]);
				testBoard.moveQueen(move[0], move[1], move[2], move[3], player);
				testBoard.throwSpear(move[4], move[5]);
			}
		}
		// now testBoard contains the nodes board state, can find all moves from that
		// board
		List<int[]> possibleMoves = testBoard.getMovesArray(testBoard, currentTurn);

		// Test each move, find their score then add them to a list to return back
		for (int[] newMove : possibleMoves) {
			RecursiveAI testMove = new RecursiveAI(testBoard.getBoard());
			testMove.moveQueen(newMove[0], newMove[1], newMove[2], newMove[3], currentTurn);
			testMove.throwSpear(newMove[4], newMove[5]);
			int moveScore = testMove.scoreMove(testMove.getBoard(), newMove, player);
			Node newChild = new Node(parentMoveList, newMove, node, moveScore);
			newChild.setChildren(null); // sets children list to null if leaf node
			discoveredNodes.add(newChild);
			//System.out.println(newChild.toString());
		}
		// updates children since no longer a leaf, used for searching
		node.setChildren(discoveredNodes);
		return discoveredNodes;
	}
	public int[] getLastMove(Node node){
		return node.getMoveList()[getNodeDepth(node) - 1];
	}
	
	public Tree(List<Node> nodes) {
		for ( Node node : nodes) {
			foundNodes.add(node);
		}
	}

	public int[] alphaBeta(Node node, int alpha, int beta) {
		int[] currentMove = new int[6];// = currentNode.getLastMove();
		int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
		
		// Handle terminal case (leaf of tree)
		if (node.getChildren() == null) {
			int[] returnVal = new int[7];
			returnVal[0] = node.getScore();
			currentMove = getLastMove(node);
			for (int i = 1; i < 7; i++) {
				returnVal[i] = currentMove[i - 1];
			}

			System.out.println("Case: terminal");
			for (int i = 0; i < 7; i++) {
				System.out.print(returnVal[i] + " ");
			}
			System.out.println();

			return returnVal;
		}

		if (this.getNodeDepth(node) % 2 == 0) { // Max turn
			for (Node child : node.getChildren()) {
				currentMove = alphaBeta(child, alpha, beta);
				
				if (node.getScore() > max) {
					max = node.getScore();
				}

				if (node.getScore() >= beta) {
					int[] returnVal = new int[7];
					//returnVal[0] = max;
					for (int i = 0; i < 7; i++) {
						returnVal[i] = currentMove[i];// - 1];
					}

					return returnVal;
				}

				if (node.getScore() > alpha) {
					alpha = node.getScore();
				}
			}

			int[] returnVal = new int[7];
			//returnVal[0] = max;
			for (int i = 0; i < 7; i++) {
				returnVal[i] = currentMove[i];
			}

			return returnVal;
		} else { // Min turn
			for (Node child : node.getChildren()) {
				currentMove = alphaBeta(child, alpha, beta);

				if (node.getScore() < min) {
					min = node.getScore();
				}

				if (node.getScore() <= alpha) {
					int[] returnVal = new int[7];
					//returnVal[0] = min;
					for (int i = 0; i < 7; i++) {
						returnVal[i] = currentMove[i];
					}

					return returnVal;
				}

				if (node.getScore() < beta) {
					beta = node.getScore();
				}
			}
			
			int[] returnVal = new int[7];
			returnVal[0] = min;
			for (int i = 1; i < 7; i++) {
				returnVal[i] = currentMove[i - 1];
			}

			return returnVal;
		}
	}
}
