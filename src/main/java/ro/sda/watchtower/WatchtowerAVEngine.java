package ro.sda.watchtower;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa ce reprezintă motorul de scanare și gestuine a bazei de date
 */
public class WatchtowerAVEngine {
    private final RBTree detailsTree;
    private final TrieByteMatcher matcher;

    /**
     * Creează un nou motor de scanare
     */
    public WatchtowerAVEngine(){
        detailsTree = new RBTree();
        matcher = new TrieByteMatcher();
    }

    /**
     * Obține numărul de definiții cunoscute de sistem
     * @return numărul de definiții cunoscute de sistem
     */
    public int getDefCount(){
        return matcher.getCount();
    }

    /**
     * Adaugă o definiție în baza de date
     * @param fileName Calea relativă spre fișierul ce conține secvența de octeți
     * @param descriptionFileName Calea relativă spre fișierul ce descrie amenințarea
     * @throws FileNotFoundException Dacă fișierul nu a putut fi găsit
     * @throws FileAlreadyExistsException Dacă un fișier din cele 2 introduse există deja în baza de date
     */
    public void add(String fileName, String descriptionFileName) throws FileNotFoundException, FileAlreadyExistsException {
        Path defPath = Path.of(fileName);
        Path descPath = Path.of(descriptionFileName);


        if(!Files.exists(defPath))
            throw new FileNotFoundException(String.format("%s does not exist",fileName));
        if(!Files.exists(descPath)){
            throw new FileNotFoundException(String.format("%s does not exist", descriptionFileName));
        }
        try {
            byte[] buff = Files.readAllBytes(defPath);

            int id = matcher.addNewSequence(buff);
            matcher.commit();

            detailsTree.add(id, descriptionFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Scanează un fișier pentru viruși
     * @param file Calea relativă spre fișierul de scanat
     * @return Listă de potriviri găsite
     * @see SequenceMatch
     */
    public List<SequenceMatch> scan(String file){
        List<SequenceMatch> matches = new ArrayList<>();
        try (RandomAccessFile aFile = new RandomAccessFile(file, "r");
             FileChannel inChannel = aFile.getChannel()) {

            //Buffer size is 1024
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                matches.addAll(matcher.tryMatchBuffer(buffer.array()));
                buffer.clear(); // do something with the data and clear/compact it.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        matcher.reset();
        return matches;
    }

    /**
     * Elimină o amenințare din baza de date
     * @param defId ID-ul amenințării
     */
    public void remove(int defId){
        matcher.deleteSequenceById(defId);
        detailsTree.remove(defId);
    }

    /**
     * Obține detaliile adiționale despre o amenințare. Se citește fișierul introdus ca fișier de descriere
     * @param threatId ID-ul amenințării
     * @return Detaliile din fișier
     */
    public String getInfo(int threatId){
        String filename = detailsTree.getForKey(threatId);
        try{
            return Files.readString(Path.of(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obține toate definițiile cunoscute
     * @return Lista de definiții cunoscute
     * @see SequenceEntry
     */
    public List<SequenceEntry> getDefinitions(){
        return matcher.getRegisteredStrings();
    }

    /**
     * Printează arborele RB aferent căilor de fișiere
     */
    public void printDescDb() {
        IO.println(detailsTree);
    }
}
