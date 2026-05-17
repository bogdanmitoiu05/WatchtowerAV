package ro.sda.watchtower;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class TrieNode {
    private final Map<Byte, TrieNode> children;
    private Integer matchId;
    private TrieNode failNode;
    private TrieNode parent;
    public TrieNode(){
        children = new TreeMap<>(); // trebuie alocat și un nod pt pozișia 0xFF
        matchId = null; // no match by default
    }

    public void markAsFinal(int matchId){
        this.matchId = matchId;
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

        children.putIfAbsent(readByte,newNode);
    }
    public TrieNode getFailNode(){
        return failNode;
    }
    public void setFailNode(TrieNode node){
        this.failNode = node;
    }
    public TrieNode advance(byte readByte){
        if(hasByte(readByte))
            return children.get(readByte);
        return failNode;
    }
}
