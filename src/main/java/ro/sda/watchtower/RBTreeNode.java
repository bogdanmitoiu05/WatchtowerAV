package ro.sda.watchtower;

public class RBTreeNode {
    private RBNodeColor color;
    private final String value;
    private final int key;
    private RBTreeNode parent;
    private RBTreeNode left;
    private RBTreeNode right;

    private RBTreeNode(int key, String value){
        this.key = key;
        this.value = value;
        this.color = RBNodeColor.RED;
    }

    public static RBTreeNode makeNode(int key, String value, RBTreeNode nullNode){
        RBTreeNode node = new RBTreeNode(key,value);
        node.setParent(nullNode);
        node.setLeft(nullNode);
        node.setRight(nullNode);
        return node;
    }
    public static RBTreeNode makeNullNode(){
        RBTreeNode node = new RBTreeNode(-1, "");
        node.setParent(node);
        node.setLeft(node);
        node.setRight(node);
        node.setColor(RBNodeColor.BLACK);
        return node;
    }

    @Override
    public String toString() {
        return "RBTreeNode{" +
                "color=" + color +
                ", value='" + value + '\'' +
                ", key=" + key +
                '}';
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

    public int getKey() {
        return key;
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
