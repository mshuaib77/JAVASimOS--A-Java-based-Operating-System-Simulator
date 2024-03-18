package userlandprocess;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Device {
	int open(String s) throws FileNotFoundException;
    void close(int id) throws IOException;
    byte[] read(int id, int size) throws IOException;
    void seek(int id, int to) throws IOException;
    int write(int id, byte[] data) throws IOException;
}
