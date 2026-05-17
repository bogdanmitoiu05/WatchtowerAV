package ro.sda.watchtower;

import org.w3c.dom.Node;

import java.util.*;
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

    public void deleteSequence(byte[] sequence) throws NoSuchElementException{
        TrieNode node = root;
        for(byte b: sequence){
            if(!node.hasByte(b)){
                throw new NoSuchElementException("Sequence not found");
            }
            node = node.advance(b);
        }
        node.unmarkFinal();
        byte transitionByte = 0;

        if(node.getChildren().isEmpty()) {
            while (node.getChildren().size() < 2) {
                transitionByte = node.getTransitionByte();
                node = node.getParent();
            }
            node.remove(transitionByte);
        }
    }
    public List<SequenceEntry> getRegisteredStrings(){
        Stack<TrieNode> stack = new Stack<>();
        Stack<TrieNode> sequenceBytes = new Stack<>();

        stack.push(root);
        List<SequenceEntry> sequences = new ArrayList<>();
        while(!stack.isEmpty()){
            TrieNode node = stack.pop();
            if(node.isFinal()){
                TrieNode seqNode = node;
                while (!seqNode.isRoot()){
                    sequenceBytes.push(seqNode);
                    seqNode = seqNode.getParent();
                }
                List<Byte> cSeq = new ArrayList<>(sequenceBytes.size());
                while(!sequenceBytes.isEmpty()){
                    cSeq.add(sequenceBytes.pop().getTransitionByte());
                }
                sequences.add(new SequenceEntry(node.getMatchId(), cSeq.toArray(new Byte[0])));
            }
            for(TrieNode n: node.getChildren().values()){
                stack.push(n);
            }
        }
        return sequences;
    }

}
