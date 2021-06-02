package amazonsChessRecursive;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private Node parentNode;
	private List<Node> children = new ArrayList<Node>();
	private int[][] moveList;
	private int score;

	public Node(int[][] prevMoves, int[] move, Node parentNode, int score) {
		this.parentNode = parentNode;
		this.score = score;
		
		if (parentNode == null) {
			this.moveList = null;
		} else {
			if (parentNode.getMoveList() == null) {
				this.moveList = new int[1][6];
				for (int i = 0; i < 6; i++) {
					this.moveList[0][i] = move[i];
				}
			} else {
				int depth = prevMoves.length + 1;
				this.moveList = new int[depth][6];
				try {
					for (int i = 0; i < depth - 1; i++) {
						for (int j = 0; j < 6; j++) {
							this.moveList[i][j] = prevMoves[i][j];
						}
					}

					for (int i = 0; i < 6; i++) {
						this.moveList[depth - 1][i] = move[i];
					}
				} catch (Exception e) {
					System.out.println("Created rooter");
				}
			}
		}
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public void setChildren(List<Node> children) {
		this.children.addAll(children);
	}

	public List<Node> getChildren() {
		return this.children;
	}

	public int[][] getMoveList() {
		return this.moveList;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return this.score;
	}

	public String toString() {
		String myMoves = "";

		for (int[] arr : this.moveList) {
			myMoves += "[";
			for (int val : arr) {
				myMoves += val + " ";
			}
			myMoves += "],";
		}

		return myMoves;
	}
}
