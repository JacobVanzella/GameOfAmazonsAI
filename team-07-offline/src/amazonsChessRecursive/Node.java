package amazonsChessRecursive;

import java.util.ArrayList;
import java.util.List;

public class Node {
	public Node parent;
	public List<Node> children;
	public int score;
	public RecursiveAI nodeBoard;
	public int counter;
	//public ArrayList<Integer> move = new ArrayList<Integer>();
	
	public Node(Node parent, int score, RecursiveAI nodeBoard, int counter) {
		this.parent = parent;
		this.score = score;
		this.nodeBoard = nodeBoard;
		this.counter = counter;
		//this.move = move;
	}
	public int getCounter() {
		return this.counter;
	}
	public Node getParent() {
		return this.parent;
	}
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	public List<Node> getChildren(){
		return this.children;
	}
	public int getScore() {
		return this.score;
	}
	public RecursiveAI getBoard() {
		return this.nodeBoard;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String toString() {
		return "\n" + score + "\n" + nodeBoard.toString();
	}
	/*
	 * public ArrayList<Integer> getMove(){ return this.move; }
	 */
}
