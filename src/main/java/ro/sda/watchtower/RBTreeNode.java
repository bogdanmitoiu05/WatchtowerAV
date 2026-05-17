package ro.sda.watchtower;

public class RBTreeNode {
    private RBNodeColor color;
    private String value;
    private int key;
    private RBTreeNode parent;
    private RBTreeNode left;
    private RBTreeNode right;

    public RBTreeNode(int key, String value){
        this.key = key;
        this.value = value;
        this.color = RBNodeColor.BLACK;
    }

    public RBTreeNode getLeft() {
        return left;
    }

    public void setLeft(RBTreeNode left) {
        this.left = left;
    }

    public RBNodeColor getColor() {
        return color;
    }

    public void setColor(RBNodeColor color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public RBTreeNode getParent() {
        return parent;
    }

    public void setParent(RBTreeNode parent) {
        this.parent = parent;
    }

    public RBTreeNode getRight() {
        return right;
    }

    public void setRight(RBTreeNode right) {
        this.right = right;
    }
}
