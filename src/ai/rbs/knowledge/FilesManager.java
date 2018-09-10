package ai.rbs.knowledge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FilesManager {
    private RandomAccessFile file;
    public final static int INDEX_SIZE_REGISTER = 2*Integer.BYTES;
    public final static int MASTER_SIZE_REGISTER = Integer.BYTES +  5*Character.BYTES + (4*Character.BYTES)*10;

    public FilesManager(String fileName) throws FileNotFoundException {
        file = new RandomAccessFile(fileName, "rw");
    }

    public String readString(int length) {
        char [] string = new char[length];
        try {
            for (int i = 0; i < string.length; i++) {
                string[i] = file.readChar();
            }
        } catch (IOException e ) { e.printStackTrace(); }
        return String.valueOf(string);
    }

    public RandomAccessFile getFile() {
        return file;
    }
}
