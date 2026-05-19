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
    private TrieNode pointer;
    private long readHead;
    public TrieByteMatcher(){
        root = new TrieNode();
        root.setFailNode(root);
        count = 0;
        pointer = root;
        readHead = 0;
    }

    public int getCount(){
        return count;
    }

    private long getMatchLength(TrieNode start){
        long size = 0;
        while(!start.isRoot()){
            ++size;
            start = start.getParent();
        }
        return size;
    }
    public List<SequenceMatch> tryMatchBuffer(byte[] buffer){
        List<SequenceMatch> matches = new ArrayList<>();
        for(byte b: buffer){
            if(pointer.isFinal()){
                long size = getMatchLength(pointer);
                SequenceMatch match = new SequenceMatch(pointer.getMatchId(), readHead - size,getMatchLength(pointer));
                matches.add(match);
            }
            pointer.advance(b);
        }
        return matches;
    }
    public void reset(){
        pointer = root;
        readHead = 0;
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
    public int addNewSequence(byte[] byteStream){
        TrieNode node = root;

        for (byte b: byteStream){
            if (!node.hasByte(b)){
                node.registerNewByte(b);
            }
            node = node.advance(b);
        }
        node.markAsFinal(count);
        ++count;
        return count-1;

    }
    public void commit(){
        configureFailNodes();
    }


    public void deleteSequenceById(int id) throws NoSuchElementException{

        Stack<TrieNode> stack = new Stack<>();
        stack.push(root);
        TrieNode node = root;
        boolean found = false;
        while (!stack.isEmpty()){
            node = stack.pop();
            if(node.isFinal() && node.getMatchId() == id)
            {
                found = true;
                break;
            }
            for(TrieNode child : node.getChildren().values()){
                stack.push(child);
            }
        }
        stack.clear();

        if(!found)
            throw new NoSuchElementException(String.format("%d not found", id));


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
