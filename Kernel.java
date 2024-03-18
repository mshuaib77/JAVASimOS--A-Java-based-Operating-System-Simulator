package userlandprocess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Semaphore;


// Assuming Scheduler and UserlandProcess classes are properly defined elsewhere
class Kernel implements Runnable, Device {
	private Thread thread;
    private Scheduler scheduler;
    private Semaphore semaphore; // Initialize the semaphore
    private VFS vfs = new VFS();
    public Kernel() {
        this.scheduler = new Scheduler();
        this.thread = new Thread(this);
        this.thread.start();
        this.semaphore = new Semaphore(0);
    }

    public void start() {
		if (!thread.isAlive()) {
			thread = new Thread(this);
			thread.start();
		}
		thread.start();
	}

	public void run() {
        while (true) {
            try {
                semaphore.acquire(); // Acquire the semaphore
                switch (OS.currentCall) {
                    case CreateProcess:
                    	  UserlandProcess up = (UserlandProcess) OS.parameters.get(0);
                          OS.Priority priority = (OS.Priority) OS.parameters.get(1);
                          CreateProcess(up, priority);
                        break;
                    case SwitchProcess:
                        scheduler.switchProcess();
                        break;
                   case Sleep:
                	   int milliseconds = (Integer) OS.parameters.get(0);
                       sleep(milliseconds);
                    	  break;
                }
                if (scheduler.getCurrentlyRunning() != null) {
                    scheduler.getCurrentlyRunning().start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace(); 
            }
        }
    }

	private void sleep(int milliseconds) {
        scheduler.sleep(milliseconds);
    }
	
	 private void CreateProcess(UserlandProcess up, OS.Priority priority) {
	        int pid = scheduler.createProcess(up, priority);
	        OS.returnValue = pid;
	    }
	public Scheduler getMyscheduler() {
		// TODO Auto-generated method stub
		return scheduler;
	}
	
	 public int open(String s) throws FileNotFoundException {
	        UserlandProcess currentProcess = scheduler.getCurrentlyRunning();
	        int[] handles = currentProcess.getDeviceIDs();

	        // Find an empty spot in the process's handle array
	        for (int i = 0; i < handles.length; i++) {
	            if (handles[i] == -1) {
	                int vfsHandle = vfs.open(s);

	                if (vfsHandle != -1) {
	                    handles[i] = vfsHandle;
	                    return i;
	                }
	                // VFS open failed, return -1
	                return -1;
	            }
	        }

	        return -1;
	    }

	    @Override
	   public void close(int id) throws IOException {
	    	// TODO Auto generated method sub
	    	}

	 public void seek(int id, int to) throws IOException {
		 UserlandProcess currentProcess = scheduler.getCurrentlyRunning();
		 int vfsId = currentProcess.getDeviceIDs()[id];

		 if(vfsId == -1)
		 {
		 throw new IllegalStateException("Device is not open!");
		 }
		 vfs.seek(vfsId, to);
	    }
	 
	 public int write(int id, byte[] data) throws IOException  {
		 UserlandProcess currentProcess = scheduler.getCurrentlyRunning();
		 int vfsId = currentProcess.getDeviceIDs()[id];

		 if(vfsId == -1)
		 {
		 throw new IllegalStateException("Device is not open!");
		 }
		 return vfs.write(vfsId, data);
		 
	    }
	 
	 
	 public byte[] read(int id, int size) throws IOException {
	        UserlandProcess currentProcess = scheduler.getCurrentlyRunning();
	        int[] handles = currentProcess.getDeviceIDs();

	        // Validate file handle index and read the device in VFS
	        if (id >= 0 && id < handles.length && handles[id] != -1) {
	            return vfs.read(handles[id], size);
	        }
	        return null;
	    }
	 
	 private int findFreeIndex( int[] a){
		 for(int i = 0; i < a.length; i++) {
			 if (a[i] == -1) {
				 return i;
				 
			 }
		 }
		 return -1;
	 }
 public int getPid() {
	 return Scheduler.getPid();
 }
}