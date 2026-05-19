package ro.sda.watchtower;

/**
 * Potrivire cu o amenințare din baza de date
 * @param id ID-ul amenințării găsite
 * @param index Offset-ul din fișier la care se găsește amenințarea
 * @param size Lungimea șirului găsit
 */
public record SequenceMatch(int id, long index, long size) {
    @Override
    public String toString() {
        return String.format("Found threat %d at index %d, size %d",id,index,size);
    }
}
