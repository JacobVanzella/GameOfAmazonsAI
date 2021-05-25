package amazonsChessRecursive;

import java.util.ArrayList;
import java.util.List;

import amazonsChess.Board;
import amazonsChess.RecursiveAI;

public class Tree {
	private Node root;
	private int depth;
	private ArrayList<Node> frontier = new ArrayList<Node>();
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
		while ((currTime - startTime) / 1000 < 20) {
			
			expandFrontier();
			
			// Update time for evaluation in while loop
			currTime = System.currentTimeMillis();
		}
	}

	public void getDepth() { // Q: this is checking the depth of the left-most branch, if this is a terminal
								// node immediatly,
								// but the others are not, wont this lead to a "false depth"
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

	public void expandFrontier() {
		Node frontierNode = this.frontier.get(0);
		List<Node> newFoundNodes = expandNode(frontierNode);
		for( Node node : newFoundNodes) {
			this.frontier.add(node);
		}
	}

	public List<Node> expandNode(Node node){
		// the new nodes that are going to be added to the frontier list.
		List<Node> discoveredNodes = new ArrayList<Node>();
		
		int[][] parentMoveList = node.getMoveList();
		
		// if depth is odd it is there move, else it is ours
		int currentTurn = (this.getNodeDepth(node) % 2 == 1) ? opponent : player;
		
		// new AI that will be used to execute move list, then find new moves
		RecursiveAI testBoard = new RecursiveAI(this.board);
		
		// makes moves to get to nodes board state (since we are storing moves to get there not the board itself)
		for( int[] move : parentMoveList ) {
			testBoard.moveQueen(move[0], move[1], move[2], move[3], player);
			testBoard.throwSpear(move[4], move[5]);
		}
		// now testBoard contains the nodes board state, can find all moves from that board
		List<int[]> possibleMoves = testBoard.getMovesArray(testBoard, currentTurn );
		
		// Test each move, find their score then add them to a list to return back
		for ( int[] newMove : possibleMoves) {
			RecursiveAI testMove = new RecursiveAI(testBoard.getBoard());
			testMove.moveQueen(newMove[0], newMove[1], newMove[2], newMove[3], currentTurn);
			testMove.throwSpear(newMove[4], newMove[5]);
			int moveScore = testMove.scoreMove(testMove.getBoard(), newMove, player);
			Node newChild = new Node(parentMoveList, newMove, node, moveScore);
			discoveredNodes.add(newChild);
		}
		return discoveredNodes;
	}

	public int getNodeDepth(Node node) {
		int nodeDepth = 0;
		if (node.getParentNode() != null) {
			nodeDepth++;
			getNodeDepth(node.getParentNode());
		} else {
			return nodeDepth;
		}
		return -1;
	}

	public void deleteFrontier() {

	}

	public Node maxValue(Node s, int alpha, int beta) {
		return null;
	}

	public Node minValue(Node s, int alpha, int beta) {
		return null;
	}
}
