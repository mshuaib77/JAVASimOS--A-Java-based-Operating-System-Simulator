package userlandprocess;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class fakeFileSystem implements Device{
	private RandomAccessFile[] files = new RandomAccessFile[10];

    @Override
    public int open(String filename)  {//if file is not found, throw exception
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i] == null) {
                try {
					files[i] = new RandomAccessFile(filename, "rw");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                return i;
            }
        }
        return -1;
    }

    @Override
    public void close(int id) throws IOException {
        if (files[id] != null) {
            files[id].close();
            files[id] = null;
        }
    }

    @Override
    public byte[] read(int id, int size) throws IOException {
        byte[] data = new byte[size];
        files[id].readFully(data);
        return data;
    }

    @Override
    public void seek(int id, int to) throws IOException {
        files[id].seek(to);
    }

    @Override
    public int write(int id, byte[] data) throws IOException {//write data to file
        long pointer = files[id].getFilePointer();
        files[id].write(data);
        return (int) (files[id].getFilePointer() - pointer);
    }
}
