package ro.sda.watchtower;

public record AdvanceResult(boolean hasFailedMatch, TrieNode node) {
}
