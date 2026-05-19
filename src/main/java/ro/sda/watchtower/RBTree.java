package ro.sda.watchtower;

import java.util.NoSuchElementException;

/**
 * Clasa ce modelează un arbore red-black
 */
public class RBTree {
    private RBTreeNode root;
    private static final RBTreeNode nullNode = RBTreeNode.makeNullNode(); //nodul null
    public RBTree(){
        root = nullNode;
    }

    /**
     * Adaugă un nod într-un arbore binar de căutare. Acest pas reprezintă prima parte a inserării într-un Red-Black Tree
     * @param key cheie de inserat
     * @param value valoarea cheii
     * @return Noul nod inserat sau un nod existent cu aceeași cheie
     */
    private RBTreeNode BSTreeAdd(int key, String value){
        if(root == nullNode){
            root = RBTreeNode.makeNode(key,value,nullNode);
            return root;
        }
        RBTreeNode node = root;
        RBTreeNode parent = root;
        while(node != nullNode){
            parent = node;
            int cKey = node.getKey();
            if(key == cKey) return node; // ignore duplicates
            if(key < cKey)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        if(key < parent.getKey()){
            RBTreeNode n = RBTreeNode.makeNode(key, value, nullNode);
            parent.setLeft(n);
            n.setParent(parent);
            return n;
        }
        else{
            RBTreeNode n = RBTreeNode.makeNode(key, value, nullNode);
            parent.setRight(n);
            n.setParent(parent);
            return n;
        }

    }

    /**
     * Găsește cheia în arbore
     * @param key Cheia de căutat în arbore
     * @return Nodul corespunzător cheii introduse sau nodul nil, dacă cheia nu există
     */
    private RBTreeNode locate(int key){
        RBTreeNode node = root;
        while(node != nullNode){
            int cKey = node.getKey();
            if(key == cKey) break; // ignore duplicates
            if(key < cKey)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        return node;
    }

    /**
     * Rotește subarborele la stânga
     * @param x Nodul pivot în jurul căruia se execută rotirea
     */
    private void rotateLeft(RBTreeNode x){
        RBTreeNode y = x.getRight();
        x.setRight(y.getLeft());
        if(y.getLeft() != null){
            y.getLeft().setParent(x);
        }
        y.setParent(x.getParent());
        if(x.getParent() == nullNode){
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

    /**
     * Rotește subarborele la dreapta
     * @param x Nodul pivot în jurul căruia se execută rotirea
     */
    private void rotateRight(RBTreeNode x){
        RBTreeNode y = x.getLeft();
        x.setLeft(y.getRight());
        if(y.getRight() != null){
            y.getRight().setParent(x);
        }
        y.setParent(x.getParent());
        if(x.getParent() == nullNode){
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

    /**
     * Funcție de reparare a proprietăților arborelui Red-Black la adunare
     * @param z Nodul de la care se începe reparația
     */
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

    /**
     * Funcție de reparare a proprietăților arborelui Red-Black la ștergere
     * @param x Nodul de la care se începe reparația
     */
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

    /**
     * Șterge cheia din arbore conform algoritmului unui arbore binar de căutare
     * @param key Cheia de ștergere
     * @throws NoSuchElementException Dacă elementul nu există
     */
    private void BSTRemove(int key) throws NoSuchElementException{
        RBTreeNode targetNode = locate(key);
        if(targetNode == nullNode)
            throw new NoSuchElementException("Item does not exist");
        RBNodeColor origColor = targetNode.getColor();
        RBTreeNode x;
        if(targetNode.getLeft() == nullNode){
            x = targetNode.getRight();
            translate(targetNode, targetNode.getRight());
        }
        else if (targetNode.getRight() == nullNode){
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

    /**
     * "Translatează" sau transplantă un subarbore la o altă locație
     * @param target Locația unde trebuie translatat subarborele
     * @param source Subarborele de translatat
     */
    private void translate(RBTreeNode target, RBTreeNode source){
        if(target.getParent() == nullNode){
            root = source;
        } else if (target == target.getParent().getLeft()) {
            target.getParent().setLeft(source);
        }
        else target.getParent().setRight(source);
        source.setParent(target.getParent());
    }

    /**
     * Obține succesorul unui nod dat
     * @param x Nodul pentru care se caută succesorul
     * @return Nodul succesor
     */
    RBTreeNode succesor(RBTreeNode x){
        RBTreeNode result = x.getRight();
        if(result != nullNode){
            while(result.getLeft() != nullNode)
                result = result.getLeft();
        }
        else{
            result = x;
            RBTreeNode parent = x.getParent();
            while(parent != nullNode && result == parent.getLeft()){
                result = parent;
                parent = parent.getParent();
            }
            result = parent;
        }
        return result;

    }

    /**
     * Adaugă o pereche cheie-valoare în arborele Red-Black
     * @param key Cheia de indexare
     * @param value valoarea
     */
    public void add(int key, String value){
        RBTreeNode n = BSTreeAdd(key,value);
        RBTreeAddFixup(n);
    }

    /**
     * Obține valoarea de la cheia precizată
     * @param key Cheia de căutare
     * @return Valoarea aferentă cheii specificate
     */
    public String getForKey(int key){
        RBTreeNode n = locate(key);
        return n.getValue();
    }

    /**
     * Execută o traversare in-order al arborelui. Folosit la afișare
     * @param node nodul actual
     * @param sb StringBuilder în care se compune output-ul final
     */
    private void inOrderTraverse(RBTreeNode node, StringBuilder sb){
        if(node.getLeft() != nullNode)
            inOrderTraverse(node.getLeft(), sb);
        sb.append(node);
        sb.append('\n');
        if(node.getRight() != nullNode)
            inOrderTraverse(node.getRight(), sb);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Traversare in-ordine:\n");
        inOrderTraverse(root,sb);
        return sb.toString();
    }

    /**
     * Elimină o cheie din arbore
     * @param key cheia de eliminat
     * @throws NoSuchElementException Dacă cheia nu există în arbore
     */
    public void remove(int key) throws NoSuchElementException{
        BSTRemove(key);
    }
}
