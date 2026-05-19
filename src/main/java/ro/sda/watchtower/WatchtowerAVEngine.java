package ro.sda.watchtower;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WatchtowerAVEngine {
    private final RBTree detailsTree;
    private final TrieByteMatcher matcher;

    public WatchtowerAVEngine(){
        detailsTree = new RBTree();
        matcher = new TrieByteMatcher();
    }

    public int getDefCount(){
        return matcher.getCount();
    }
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
    public void remove(int defId){
        matcher.deleteSequenceById(defId);
        detailsTree.remove(defId);
    }
    public String getInfo(int threatId){
        String filename = detailsTree.getForKey(threatId);
        try{
            return Files.readString(Path.of(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<SequenceEntry> getDefinitions(){
        return matcher.getRegisteredStrings();
    }

    public void printDescDb() {
        IO.println(detailsTree);
    }
}
