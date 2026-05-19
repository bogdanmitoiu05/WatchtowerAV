package ro.sda.watchtower;
import java.util.*;

/**
 * Nod de arbore trie
 */
public class TrieNode {
    private final Map<Byte, TrieNode> children;
    private Integer matchId;
    private TrieNode failNode;
    private Byte transitionByte;
    private TrieNode parent;

    /**
     * Creează un nod de arbore trie
     */
    public TrieNode(){
        children = new HashMap<>(); // trebuie alocat și un nod pt pozișia 0xFF
        matchId = null; // no match by default
        transitionByte = null;
    }

    /**
     * Setează octetul de tranziție
     * @param b Octetul de tranziție
     */
    public void setTransitionByte(Byte b){
        transitionByte = b;
    }

    /**
     * Obține octetul de tranziție
     * @return Octetul de tranziție
     */
    public Byte getTransitionByte() {
        return transitionByte;
    }

    /**
     * Marchează nodul curent ca nod final. Acesta va primi ID-ul pasat ca argument ce va corespunde cu ID-ul secvenței
     * @param matchId ID-ul secvenței
     */
    public void markAsFinal(int matchId){
        this.matchId = matchId;
    }

    /**
     * Șterge marcajul de final.
     * @apiNote Trebuie apelată în combinație cu <code>remove()</code>
     */
    public void unmarkFinal(){
        this.matchId = null;
    }

    /**
     * Verifică dacă nodul curent este un nod final
     * @return Valoare booleană true/false coresp. descrierii
     */
    public boolean isFinal(){
        return matchId != null;
    }

    /**
     * Obține ID-ul pentru potrivirea curentă
     * @return ID-ul pentru potrivirea curentă sau null, dacă nodul nu este terminal
     * @apiNote Asigurați-vă că acest nod este final cu <code>isFinal()</code> înainte de a apela această funcție
     */
    public Integer getMatchId(){
        return matchId;
    }

    /**
     * Obține părintele nodului curent
     * @return Părintele nodului curent
     */
    public TrieNode getParent(){
        return parent;
    }

    /**
     * Setează părintele pentru nodul curent
     * @param node Noul părinte pentru nodul curent
     */
    public void setParent(TrieNode node){
        this.parent = node;
    }

    /**
     * Obține copii nodului curent în format (byte_tranziție, nod)
     * @return Copii nodului curent în format (byte_tranziție, nod)
     */
    public Map<Byte, TrieNode> getChildren() {
        return children;
    }

    /**
     * Verifică dacă nodul curent este rădăcină
     * @return Valoare booleană corespunzătoare verificării
     */
    public boolean isRoot(){
        return parent == null;
    }

    /**
     * Verifică dacă nodul are printre copii un anumit octet
     * @param readByte Octetul de căutat
     * @return Valoare booleană corespunzătoare verificării
     */
    public boolean hasByte(byte readByte){
        return children.containsKey(readByte);
    }

    /**
     * Adaugă un nou octet acestui nod. Va crea un nou nod.
     * @param readByte Octetul coresp. tranziției
     */
    public void registerNewByte(byte readByte){
        TrieNode newNode = new TrieNode();
        newNode.setParent(this);
        newNode.setTransitionByte(readByte);

        children.putIfAbsent(readByte,newNode);
    }

    /**
     * Obține legătura de eșec
     * @return Legătura de eșec
     */
    public TrieNode getFailNode(){
        return failNode;
    }

    /**
     * Setează șegătura de eșec
     * @param node Noua legătură de eșec
     */
    public void setFailNode(TrieNode node){
        this.failNode = node;
    }

    /**
     * Coboară în arbore după octet
     * @param readByte Octetul pentru care se excută tranziția
     * @return Rezultatul de avansare
     * @see AdvanceResult
     */
    public AdvanceResult advance(byte readByte){
        if(hasByte(readByte))
            return new AdvanceResult(false, children.get(readByte));
        return new AdvanceResult(true, failNode);
    }

    /**
     * Elimină un nod din copii nodului curent.
     * @param transitionByte Octetul de tranziție ce trebuie șters
     * @apiNote Se apelează exclusiv de TrieByteMatcher
     */
    public void remove(byte transitionByte) {
        children.remove(transitionByte);
    }
}
