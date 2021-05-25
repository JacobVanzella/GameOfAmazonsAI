package amazonsChessRecursive;

import java.util.ArrayList;
import java.util.List;

public class Node {

	public Node parentNode; 
	public List<Node> children; 
	public String data;

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public List<Node> getChildren() {
		return this.getChildren();
	}

	public String getDataValue() {
		return data;
	}

	public void String(String dataValue) {
		this.data = dataValue;
	}

	public static int getBranchingFactor() {
		return branchingFactor;
	}

	public static void setMaxNumberOfChildren(int branchingFactor) {
		Node.branchingFactor = branchingFactor;
	}

	public static int branchingFactor; 

	public Node(String data) {
		this.data = data;
		ArrayList<Node> children = new ArrayList<Node>(branchingFactor);
	}

	public void addChild(Node childNaryTreeNode, int position) throws Exception {
		if (position >= branchingFactor - 1) {
			throw new Exception("Max number of childeren reached");
		} else {
			System.out.println("this.children=" + this.children);
			if (this.children.get(position) != null) { 
			} else {
				childNaryTreeNode.parentNode = this;
				this.children.set(position, childNaryTreeNode);
			}
		}
	}
}
