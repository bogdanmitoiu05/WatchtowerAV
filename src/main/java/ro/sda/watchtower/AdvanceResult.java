package ro.sda.watchtower;

/**
 * Rezultatul unei operații de coborâre într-un arbore Trie
 * @param hasFailedMatch True, dacă s-a urmat legătura de eșec
 * @param node Nodul rezultat
 */
public record AdvanceResult(boolean hasFailedMatch, TrieNode node) {
}
