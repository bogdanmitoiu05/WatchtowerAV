package ro.sda.watchtower;

import java.util.*;

/**
 * Clasa pentru implementarea arborilor Trie
 * @see TrieNode
 */
public class TrieByteMatcher {

    private final TrieNode root;
    private int count;
    private TrieNode pointer;
    private long readHead;
    private final Queue<Integer> emptySlots; // pentru momentele când se șterge un id care nu este exact la extremitatea dreaptă

    /**
     * Creează un nou arbore Trie. Acest constructor inițializează nodul rădăcină, pointerul de căutare, capul de citire și numărul de intrări
     */
    public TrieByteMatcher(){
        root = new TrieNode();
        root.setFailNode(root);
        count = 0;
        pointer = root;
        readHead = 0;
        emptySlots = new LinkedList<>();
    }

    /**
     * Obține numărul de înregistrări din arbore
     * @return Numărul de înregistrări din arbore
     */
    public int getCount(){
        return count;
    }

    /**
     * Obține lungimea unei potriviri din arbore. Această funcție numără de jos în sus
     * @param start Nodul de la care se pornește
     * @return Numărul de caractere pentru o potrivire
     */
    private long getMatchLength(TrieNode start){
        long size = 0;
        while(!start.isRoot()){
            ++size;
            start = start.getParent();
        }
        return size;
    }

    /**
     * Funcție ce primește un buffer de octeți și încearcă să găsească șabloanele stocare în arbore. De reținut că această funcție <b>modifică starea internă</b>
     * a arborelui, deoarece se păstrează un indicator (pointer) ce se mișcă în interiorul arborelui pentru a facilita căutarea pe intrări mai mari decât mărimea
     * unui buffer. Pentru a trece la un alt fișier, trebuie să se apeleze <code>reset()</code>
     * @param buffer Bufferul în care se caută
     * @return Listă de potriviri
     * @see SequenceMatch
     */
    public List<SequenceMatch> tryMatchBuffer(byte[] buffer){
        List<SequenceMatch> matches = new ArrayList<>();
        for(byte b: buffer){
            if(pointer.isFinal()){
                long size = getMatchLength(pointer);
                SequenceMatch match = new SequenceMatch(pointer.getMatchId(), readHead - size,getMatchLength(pointer));
                matches.add(match);
            }
            AdvanceResult result = pointer.advance(b);
            if(result.hasFailedMatch())
                result = result.node().advance(b);
            pointer = result.node();
            ++readHead;
        }
        return matches;
    }

    /**
     * Resetează indicatorul pentru a începe scanarea unui nou fișier
     */
    public void reset(){
        pointer = root;
        readHead = 0;
    }

    /**
     * Configurează legăturile de eșec pentru un nod dat
     * @param node Perechea de octet și nod necesară algoritmului
     */
    private void configureFailNodesForNode(TrieNode node){
        TrieNode orig = node;
        Byte b = node.getTransitionByte();
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

    /**
     * Configurează toate legăturile de eșec pentru arbore
     */
    private void configureFailNodes(){
        Queue<TrieNode> nodes = new LinkedList<>();
        nodes.add(root);
        while (!nodes.isEmpty()){
            TrieNode node = nodes.remove();

            nodes.addAll(node.getChildren().values());
            configureFailNodesForNode(node);
        }
    }

    /**
     * Introduce o nouă secvență de octeți în baza de date a arborelui. După finalizarea tuturor operațiilor de adăugare trebuie să se apeleze <code>commit()</code>.
     * @param byteStream Buffer de octeți
     * @return ID-ul secvenței
     */
    public int addNewSequence(byte[] byteStream){
        TrieNode node = root;

        for (byte b: byteStream){
            if (!node.hasByte(b)){
                node.registerNewByte(b);
            }
            node = node.advance(b).node();
        }
        if(emptySlots.isEmpty())
            node.markAsFinal(count);
        else
            node.markAsFinal(emptySlots.remove());
        ++count;
        return count-1;

    }

    /**
     * Execută post-procesarea necesară funcționării algoritmului de căutare
     */
    public void commit(){
        configureFailNodes();
    }


    /**
     * Șterge secvența de octeți aferentă indexului dat
     * @param id Indexul secvenței de șters
     * @throws NoSuchElementException În cazul în care se introduce un index ce nu există în arbore
     */
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
        if(id != count-1)
            emptySlots.add(id);
        --count;
    }

    /**
     * Obține toate secvențele de octeți înregistrate în arbore
     * @return Lista de secvențe
     * @see SequenceEntry
     */
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
