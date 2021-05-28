package amazonsChessRecursive;

import java.util.ArrayList;
import java.util.List;

import amazonsChess.Board;
import amazonsChess.RecursiveAI;

public class Tree {
	public Node root;
	private int depth;
	private ArrayList<Node> frontier = new ArrayList<Node>();
	public ArrayList<Node> foundNodes = new ArrayList<Node>();
	private int[][] board = new int[10][10];
	int player = 0;
	int opponent = 0;
	Long secondsElapsed = Long.MIN_VALUE;
	Long startTime = Long.MIN_VALUE;
	boolean keepGoing = true;
	int runTime = 0;
	boolean validMove = true;

	public Tree(RecursiveAI currBoard, int player) {
		this.root = new Node(null, null, null, Integer.MIN_VALUE);
		this.player = player;
		this.opponent = (this.player == 1) ? 2 : 1;

		frontier.add(root);

		// Initializes timer
		this.startTime = System.currentTimeMillis();

		// Stores version of the board locally
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				this.board[j][i] = currBoard.getTile(j, i);
			}
		}

		// set amount of time to run the AI
		// runTime only controls time to generate the nodes
		// therefore want to leave a 2-3 seconds for the alpha-beta search.
		this.runTime = 1;

		// will keep going until expandNode runs out of alloted time
		while (this.keepGoing) {

			// expandFrontier calls expandNode which will break when run out of time
			expandFrontier();

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

	public boolean validMove() {
		return this.validMove;
	}

	public void expandFrontier() {
		if (frontier.size() == 0) {
			this.validMove = false;
			this.keepGoing = false;
		} else {
			Node frontierNode = this.frontier.get(0);
			List<Node> newFoundNodes = expandNode(frontierNode);

			this.foundNodes.add(frontierNode); // if errors, check these children
			for (Node node : newFoundNodes) {
				this.frontier.add(node);
			}
			this.frontier.remove(0);
		}
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
				// System.out.println(
				// move[0] + "," + move[1] + "->" + move[2] + "," + move[3] + " Spear:" +
				// move[4] + "," + move[5]);
				testBoard.moveQueen(move[0], move[1], move[2], move[3], player);
				testBoard.throwSpear(move[4], move[5]);
			}
		}
		// now testBoard contains the nodes board state, can find all moves from that
		// board
		List<int[]> possibleMoves = testBoard.getMovesArray(testBoard, currentTurn);

		// Test each move, find their score then add them to a list to return back
		for (int[] newMove : possibleMoves) {

			// test if enough time to check child, if there is not, break out of loop.
			Long currTime = System.currentTimeMillis();
			this.secondsElapsed = (currTime - this.startTime) / 1000;
			if (this.secondsElapsed > this.runTime) {
				this.keepGoing = false;
				break;
			}

			RecursiveAI testMove = new RecursiveAI(testBoard.getBoard());
			testMove.moveQueen(newMove[0], newMove[1], newMove[2], newMove[3], currentTurn);
			testMove.throwSpear(newMove[4], newMove[5]);
			int moveScore = testMove.scoreMove(testMove.getBoard(), newMove, player);
			Node newChild = new Node(parentMoveList, newMove, node, moveScore);
			// newChild.setChildren(null); // sets children list to null if leaf node
			discoveredNodes.add(newChild);
			// System.out.println(newChild.toString());
		}
		// updates children since no longer a leaf, used for searching
		node.setChildren(discoveredNodes);
		return discoveredNodes;
	}

	public int[] getLastMove(Node node) {
		if (node.getMoveList() != null) {
			return node.getMoveList()[getNodeDepth(node) - 1];
		} else {
			return new int[6];
		}
	}

	public Long timeElapsed() {
		Long currTime = System.currentTimeMillis();
		this.secondsElapsed = (currTime - this.startTime) / 1000;
		return this.secondsElapsed;
	}

	public Tree(List<Node> nodes) {
		for (Node node : nodes) {
			foundNodes.add(node);
		}
	}

	public int[] alphaBeta(Node node, int alpha, int beta) {
		// int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;

		// Handle terminal case (leaf of tree)
		if (node.getChildren().isEmpty()) {
			int[] currentMove = new int[6];
			int[] returnVal = new int[7];
			returnVal[0] = node.getScore();
			currentMove = getLastMove(node);
			for (int i = 1; i < 7; i++) {
				returnVal[i] = currentMove[i - 1];
			}

			return returnVal;
		}
		int[] currentMove = new int[7];

		if (this.getNodeDepth(node) % 2 == 0) { // Max turn
			int[] max = new int[7];
			max[0] = Integer.MIN_VALUE;
			for (Node child : node.getChildren()) {
				int[] childMove = getLastMove(child);
				currentMove = alphaBeta(child, alpha, beta);

				// max = Math.max(max.score, currentMove.score);
				if (currentMove[0] > max[0]) {
					max[0] = currentMove[0];
					for (int i = 0; i < 6; i++) {
						max[i + 1] = childMove[i];
					}
				}

				// alpha = Math.max(max.score, alpha);
				if (max[0] > alpha) {
					alpha = max[0];
				}

				// if beta <= alpha can prune branch
				if (beta <= alpha) {
					break;
				}
			}

			// return max - which is the move array
			int[] returnValue = new int[7];
			for (int i = 0; i < 7; i++) {
				returnValue[i] = max[i];
			}
			return returnValue;

		} else { // Min turn
			int[] min = new int[7];
			min[0] = Integer.MAX_VALUE;
			for (Node child : node.getChildren()) {
				int[] childMove = getLastMove(child);
				currentMove = alphaBeta(child, alpha, beta);

				// min = Math.min( min.score, currentMove.score);
				if (currentMove[0] < min[0]) {
					min[0] = currentMove[0];
					for (int i = 0; i < 6; i++) {
						min[i + 1] = childMove[i];
					}
				}

				// beta = Math.min( min, beta)
				if (min[0] < beta) {
					beta = min[0];
				}

				// if beta <= alpha can prune branch
				if (beta <= alpha) {
					break;
				}

			}

			// return min - which is the move array
			int[] returnValue = new int[7];
			for (int i = 0; i < 7; i++) {
				returnValue[i] = min[i];
			}
			return returnValue;
		}
	}
}
