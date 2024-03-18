package userlandprocess;
import java.util.Random;

public class RandomDevice implements Device {
	  private Random[] devices = new Random[10];

	    @Override
	    public int open(String s) {
	        for (int i = 0; i < devices.length; i++) {
	            if (devices[i] == null) {
	                devices[i] = (s != null && !s.isEmpty()) ? new Random(Integer.parseInt(s)) : new Random(); // If s is not null or empty, use it as the seed for the Random object
	                return i;
	            }
	        }
	        return -1;
	    }

	    @Override
	    public void close(int id) {
	        devices[id] = null;
	    }

	    @Override
	    public byte[] read(int id, int size) {
	        byte[] data = new byte[size];
	        devices[id].nextBytes(data);
	        return data;
	    }

	    @Override
	    public void seek(int id, int to) {
	        // Do nothing for RandomDevice
	    }

	    @Override
	    public int write(int id, byte[] data) {
	        return 0; // Do nothing for RandomDevice
	    }	

}
