package ro.sda.watchtower;

public class RBTreeNode {
    private RBNodeColor color;
    private final String value;
    private final int key;
    private RBTreeNode parent;
    private RBTreeNode left;
    private RBTreeNode right;

    /**
     * Constructor privat folosit de funcțiile factory
     * @param key Cheia nodului
     * @param value Valoarea nodului
     */
    private RBTreeNode(int key, String value){
        this.key = key;
        this.value = value;
        this.color = RBNodeColor.RED;
    }

    /**
     * Instanțiază un nod. La creere, acesta va fi ROȘU
     * @param key Cheia noului nod
     * @param value Valoarea noului nod
     */
    public static RBTreeNode makeNode(int key, String value, RBTreeNode nullNode){
        RBTreeNode node = new RBTreeNode(key,value);
        node.setParent(nullNode);
        node.setLeft(nullNode);
        node.setRight(nullNode);
        return node;
    }

    /**
     * Instanțiază nodul nil.
     * @implNote  Această funcție se apelează doar o singură dată
     * @return Nodul nil
     */
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

    /**
     * Obține nodul din stânga nodului curent
     * @return Nodul din stânga nodului curent
     */
    public RBTreeNode getLeft() {
        return left;
    }

    /**
     * Setează copilul din stânga nodului curent
     * @param left Noul copil din stânga pentru nodul curent
     */
    public void setLeft(RBTreeNode left) {
        this.left = left;
    }
    /**
     * Obține culoarea nodului curent
     * @return Culoarea nodului curent
     */
    public RBNodeColor getColor() {
        return color;
    }

    /**
     * Setează culoarea nodului curent
     * @param color Noua culoare a nodului curent
     */
    public void setColor(RBNodeColor color) {
        this.color = color;
    }
    /**
     * Obține valoarea nodului curent
     * @return Valoarea nodului curent
     */
    public String getValue() {
        return value;
    }
    /**
     * Obține cheia nodului curent
     * @return Cheia nodului curent
     */
    public int getKey() {
        return key;
    }
    /**
     * Obține părintele nodului curent
     * @return Părintele nodului curent
     */
    public RBTreeNode getParent() {
        return parent;
    }
    /**
     * Setează părintele nodului curent
     * @param parent Noul părinte pentru nodul curent
     */
    public void setParent(RBTreeNode parent) {
        this.parent = parent;
    }
    /**
     * Obține nodul din dreapta nodului curent
     * @return Nodul din dreapta nodului curent
     */
    public RBTreeNode getRight() {
        return right;
    }
    /**
     * Setează copilul din dreapta nodului curent
     * @param right Noul copil din dreapta pentru nodul curent
     */
    public void setRight(RBTreeNode right) {
        this.right = right;
    }
}
