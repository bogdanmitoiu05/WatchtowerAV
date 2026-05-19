package ro.sda.watchtower;

public record FileMatch(String file, long offset, long size, int id) {
}
