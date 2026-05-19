package ro.sda.watchtower;

public record SequenceMatch(int id, long index, long size) {
    @Override
    public String toString() {
        return String.format("Found threat %d at index %d, size %d",id,index,size);
    }
}
