package ro.sda.watchtower;

/**
 * Pereche ce reprezintă o intrare în baza de date de amenințări
 * @param id ID-ul amenințării
 * @param bytes Secvența de octeți aferentă amenințării
 */
public record SequenceEntry(int id, Byte[] bytes) {
}
