package ro.sda.watchtower;

import java.util.NoSuchElementException;

public class RBTree {
    private RBTreeNode root;
    public RBTree(){
        root = RBTreeNode.nullNode;
    }

    private RBTreeNode BSTreeAdd(int key, String value){
        if(root == RBTreeNode.nullNode){
            root = new RBTreeNode(key,value);
            return root;
        }
        RBTreeNode node = root;
        RBTreeNode parent = root;
        while(node != RBTreeNode.nullNode){
            parent = node;
            int cKey = node.getKey();
            if(key == cKey) return node; // ignore duplicates
            if(key < cKey)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        if(key < parent.getKey()){
            RBTreeNode n = new RBTreeNode(key, value);
            parent.setLeft(n);
            n.setParent(parent);
            return n;
        }
        else{
            RBTreeNode n = new RBTreeNode(key, value);
            parent.setRight(n);
            n.setParent(parent);
            return n;
        }

    }

    private RBTreeNode locate(int key){
        RBTreeNode node = root;
        while(node != RBTreeNode.nullNode){
            int cKey = node.getKey();
            if(key == cKey) break; // ignore duplicates
            if(key < cKey)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        return node;
    }
    private void rotateLeft(RBTreeNode x){
        RBTreeNode y = x.getRight();
        x.setRight(y.getLeft());
        if(y.getLeft() != null){
            y.getLeft().setParent(x);
        }
        y.setParent(x.getParent());
        if(x.getParent() == RBTreeNode.nullNode){
            this.root = y;
        } else if (x == x.getParent().getLeft()) {
            x.getParent().setLeft(y);
        }
        else {
            x.getParent().setLeft(y);
        }
        y.setLeft(x);
        x.setParent(y);
    }

    private void rotateRight(RBTreeNode x){
        RBTreeNode y = x.getLeft();
        x.setLeft(y.getRight());
        if(y.getRight() != null){
            y.getRight().setParent(x);
        }
        y.setParent(x.getParent());
        if(x.getParent() == RBTreeNode.nullNode){
            this.root = y;
        } else if (x == x.getParent().getRight()) {
            x.getParent().setRight(y);
        }
        else {
            x.getParent().setRight(y);
        }
        y.setRight(x);
        x.setParent(y);
    }
    private void RBTreeAddFixup(RBTreeNode z){
        while (z.getParent().getColor() == RBNodeColor.RED){
            if(z.getParent() == z.getParent().getParent().getLeft()){
               RBTreeNode y = z.getParent().getParent().getRight();
               if(y.getColor() == RBNodeColor.RED){
                   z.getParent().setColor(RBNodeColor.BLACK);
                   y.setColor(RBNodeColor.BLACK);
                   z.getParent().getParent().setColor(RBNodeColor.RED);
                   z = z.getParent().getParent();
               }
               else{
                   if(z == z.getParent().getRight()){
                       z = z.getParent();
                       rotateLeft(z);
                   }
                   z.getParent().setColor(RBNodeColor.BLACK);
                   z.getParent().getParent().setColor(RBNodeColor.RED);
                   rotateRight(z.getParent().getParent());
               }
            }
            else{
                RBTreeNode y = z.getParent().getParent().getLeft();
                if(y.getColor() == RBNodeColor.RED){
                    z.getParent().setColor(RBNodeColor.BLACK);
                    y.setColor(RBNodeColor.BLACK);
                    z.getParent().getParent().setColor(RBNodeColor.RED);
                    z = z.getParent().getParent();
                }
                else{
                    if(z == z.getParent().getLeft()){
                        z = z.getParent();
                        rotateRight(z);
                    }
                    z.getParent().setColor(RBNodeColor.BLACK);
                    z.getParent().getParent().setColor(RBNodeColor.RED);
                    rotateLeft(z.getParent().getParent());
                }
            }
        }
        root.setColor(RBNodeColor.BLACK);
    }

    private void RBTreeRemoveFixup(RBTreeNode x){
        while (x != root && x.getColor() == RBNodeColor.BLACK){
            if(x == x.getParent().getLeft()){
                RBTreeNode w = x.getParent().getRight();
                if(w.getColor() == RBNodeColor.RED){
                    w.setColor(RBNodeColor.BLACK);
                    x.getParent().setColor(RBNodeColor.RED);
                    rotateLeft(x.getParent());
                    w = x.getParent().getRight();
                }
                if(w.getLeft().getColor() == RBNodeColor.BLACK && w.getRight().getColor() == RBNodeColor.BLACK){
                    w.setColor(RBNodeColor.RED);
                    x = x.getParent();
                }
                else{
                    if(w.getRight().getColor() == RBNodeColor.BLACK){
                        w.getLeft().setColor(RBNodeColor.BLACK);
                        w.setColor(RBNodeColor.BLACK);
                        rotateRight(w);
                        w = x.getParent().getRight();
                    }
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(RBNodeColor.BLACK);
                    w.getRight().setColor(RBNodeColor.BLACK);
                    rotateLeft(x.getParent());
                    x = root;
                }
            }
            else{
                RBTreeNode w = x.getParent().getLeft();
                if(w.getColor() == RBNodeColor.RED){
                    w.setColor(RBNodeColor.BLACK);
                    x.getParent().setColor(RBNodeColor.RED);
                    rotateRight(x.getParent());
                    w = x.getParent().getLeft();
                }
                if(w.getRight().getColor() == RBNodeColor.BLACK && w.getLeft().getColor() == RBNodeColor.BLACK){
                    w.setColor(RBNodeColor.RED);
                    x = x.getParent();
                }
                else{
                    if(w.getLeft().getColor() == RBNodeColor.BLACK){
                        w.getRight().setColor(RBNodeColor.BLACK);
                        w.setColor(RBNodeColor.BLACK);
                        rotateRight(w);
                        w = x.getParent().getLeft();
                    }
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(RBNodeColor.BLACK);
                    w.getLeft().setColor(RBNodeColor.BLACK);
                    rotateRight(x.getParent());
                    x = root;
                }
            }
        }
        x.setColor(RBNodeColor.BLACK);
    }
    private void BSTRemove(int key) throws NoSuchElementException{
        RBTreeNode targetNode = locate(key);
        if(targetNode == RBTreeNode.nullNode)
            throw new NoSuchElementException("Item does not exist");
        RBNodeColor origColor = targetNode.getColor();
        RBTreeNode x;
        if(targetNode.getLeft() == RBTreeNode.nullNode){
            x = targetNode.getRight();
            translate(targetNode, targetNode.getRight());
        }
        else if (targetNode.getRight() == RBTreeNode.nullNode){
            x = targetNode.getLeft();
            translate(targetNode, targetNode.getLeft());
        }
        else{
            RBTreeNode successor = succesor(targetNode);
            origColor = successor.getColor();
            x = successor.getRight();
            if (successor != targetNode.getRight()){
                translate(successor, successor.getRight());
                successor.setRight(targetNode.getRight());
                successor.getRight().setParent(successor);
            }
            else{
                x.setParent(successor);
            }
            translate(targetNode, successor);
            successor.setLeft(targetNode.getLeft());
            successor.getLeft().setParent(successor);
            successor.setColor(targetNode.getColor());
        }
        if(origColor == RBNodeColor.BLACK)
            RBTreeRemoveFixup(x);
    }
    private void translate(RBTreeNode target, RBTreeNode source){
        if(target.getParent() == RBTreeNode.nullNode){
            root = source;
        } else if (target == target.getParent().getLeft()) {
            target.getParent().setLeft(source);
        }
        else target.getParent().setRight(source);
        source.setParent(target.getParent());
    }
    RBTreeNode succesor(RBTreeNode x){
        RBTreeNode result = x.getRight();
        if(result != RBTreeNode.nullNode){
            while(result.getLeft() != RBTreeNode.nullNode)
                result = result.getLeft();
        }
        else{
            result = x;
            RBTreeNode parent = x.getParent();
            while(parent != RBTreeNode.nullNode && result == parent.getLeft()){
                result = parent;
                parent = parent.getParent();
            }
            result = parent;
        }
        return result;

    }

    public void add(int key, String value){
        RBTreeNode n = BSTreeAdd(key,value);
        RBTreeAddFixup(n);
    }

    public String getForKey(int key){
        RBTreeNode n = locate(key);
        return n.getValue();
    }

    public void remove(int key) throws NoSuchElementException{
        BSTRemove(key);
    }
}
