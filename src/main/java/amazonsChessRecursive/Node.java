package amazonsChessRecursive;

import java.util.ArrayList;
import java.util.List;
 
public class Node {
 
    public Node parentNode; // The parent of the current node
    public List<Node> childList; // The children's of the current node
    public String dataValue;
 
    public Node getParentNode() {
        return parentNode;
    }
 
    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }
 
 
    public void setChildren(List<Node> childList) {
        this.childList = childList;
    }
    public List<Node> getChildren(){
    	return this.getChildren();
    }
 
    public String getDataValue() {
        return dataValue;
    }
 
    public void String(String dataValue) {
        this.dataValue = dataValue;
    }
 
    public static int getMaxNumberOfChildren() {
        return maxNumberOfChildren;
    }
 
    public static void setMaxNumberOfChildren(int maxNumberOfChildren) {
        Node.maxNumberOfChildren = maxNumberOfChildren;
    }
 
    public static int maxNumberOfChildren; // Equal to the n-arity;
 
    public Node(String dataValue) {
        this.dataValue = dataValue;
        ArrayList<Node> children = new ArrayList<Node>(maxNumberOfChildren);
    }
 
    public void addChild(Node childNaryTreeNode, int position) throws Exception {
        if (position >= maxNumberOfChildren - 1) {
            throw new Exception("Max number of childeren reached");
        } else {
            System.out.println("this.children=" + this.childList);
            if (this.childList.get(position) != null) {
                // There is already a child node on this position; throw some error;
            } else {
                childNaryTreeNode.parentNode = this;
                this.childList.set(position, childNaryTreeNode);
            }
        }
    }
}
