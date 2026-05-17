package ro.sda.watchtower;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * Clasa pentru implementarea arborilor Tries
 */
public class TrieByteMatcher {

    private final TrieNode root;
    private int count;
    public TrieByteMatcher(){
        root = new TrieNode();
        root.setFailNode(root);
        count = 0;
    }

    public int getCount(){
        return count;
    }
    private void configureFailNodesForNode(ByteAndNode pair){
        TrieNode orig = pair.node();
        TrieNode node = pair.node();
        Byte b = pair.b();
        if(node.isRoot()) return;
        node = node.getParent().getFailNode();

        while (!node.isRoot()){
            if(node.hasByte(b)){
                node.advance(b);
                break;
            }
            node = node.getFailNode();
        }
        orig.setFailNode(node);
    }
    private void configureFailNodes(){
        Queue<ByteAndNode> nodes = new LinkedList<>();
        nodes.add(new ByteAndNode(null, root));
        while (!nodes.isEmpty()){
            ByteAndNode pair = nodes.remove();
            TrieNode node = pair.node();

            for(Byte b: node.getChildren().keySet()){
                nodes.add(new ByteAndNode(b, node.advance(b)));
            }
            configureFailNodesForNode(pair);
        }
    }
    public void addNewSequence(byte[] byteStream){
        TrieNode node = root;
        ++count;
        for (byte b: byteStream){
            if (!node.hasByte(b)){
                node.registerNewByte(b);
            }
            node = node.advance(b);
        }
        node.markAsFinal(count);

    }
    public void commit(){
        configureFailNodes();
    }


}
