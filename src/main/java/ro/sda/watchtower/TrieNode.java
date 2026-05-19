package ro.sda.watchtower;
import java.util.*;

public class TrieNode {
    private final Map<Byte, TrieNode> children;
    private Integer matchId;
    private TrieNode failNode;
    private Byte transitionByte;
    private TrieNode parent;
    public TrieNode(){
        children = new HashMap<>(); // trebuie alocat și un nod pt pozișia 0xFF
        matchId = null; // no match by default
        transitionByte = null;
    }

    public void setTransitionByte(Byte b){
        transitionByte = b;
    }
    public Byte getTransitionByte() {
        return transitionByte;
    }

    public void markAsFinal(int matchId){
        this.matchId = matchId;
    }
    public void unmarkFinal(){
        this.matchId = null;
    }
    public boolean isFinal(){
        return matchId != null;
    }
    public Integer getMatchId(){
        return matchId;
    }
    public TrieNode getParent(){
        return parent;
    }
    public void setParent(TrieNode node){
        this.parent = node;
    }

    public Map<Byte, TrieNode> getChildren() {
        return children;
    }

    public boolean isRoot(){
        return parent == null;
    }
    public boolean hasByte(byte readByte){
        return children.containsKey(readByte);
    }
    public void registerNewByte(byte readByte){
        TrieNode newNode = new TrieNode();
        newNode.setParent(this);
        newNode.setTransitionByte(readByte);

        children.putIfAbsent(readByte,newNode);
    }
    public TrieNode getFailNode(){
        return failNode;
    }
    public void setFailNode(TrieNode node){
        this.failNode = node;
    }
    public AdvanceResult advance(byte readByte){
        if(hasByte(readByte))
            return new AdvanceResult(false, children.get(readByte));
        return new AdvanceResult(true, failNode);
    }

    public void remove(byte transitionByte) {
        children.remove(transitionByte);
    }
}
