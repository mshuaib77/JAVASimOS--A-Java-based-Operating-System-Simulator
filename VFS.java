package userlandprocess;
import java.io.FileNotFoundException;
import java.io.IOException;

public class VFS implements Device {
    private final Device[] devices = new Device[10];
    private final int[] ids = new int[10];
    private final RandomDevice randomDevice = new RandomDevice();
    private final fakeFileSystem fakeFileSystem = new fakeFileSystem();


    public int open(String s) throws FileNotFoundException {
        String[] parts = s.split(" ", 2);
        String deviceType = parts[0];
        String param = parts.length > 1 ? parts[1] : "";

        Device device;
        if ("random".equals(deviceType)) {
            device = randomDevice;
        } else if ("file".equals(deviceType)) {
            device = fakeFileSystem;
        } else {
            return -1;
        }

        int deviceId = device.open(param);
        if (deviceId == -1) {
            return -1;
        }

        for (int i = 0; i < devices.length; i++) {
            if (devices[i] == null) {
                devices[i] = device;
                ids[i] = deviceId;
                return i;
            }
        }
        return -1;
    }


    public void close(int id) throws IOException {
        devices[id].close(ids[id]);
        devices[id] = null;
        ids[id] = -1;
    }


    public byte[] read(int id, int size) throws IOException {
        return devices[id].read(ids[id], size);
    }


    public void seek(int id, int to) throws IOException {
        devices[id].seek(ids[id], to);
    }


    public int write(int id, byte[] data) throws IOException {
        return devices[id].write(ids[id], data);
    }
}
