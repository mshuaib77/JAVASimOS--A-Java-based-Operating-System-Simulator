package userlandprocess;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

import userlandprocess.OS.Priority;
import java.io.FileNotFoundException;


public abstract class UserlandProcess implements Runnable {

	
	public static final int pid = 0;
	private Thread thread;
    private Semaphore semaphore = new Semaphore(0);
    private boolean quantumExpired = false;
    private PCB pcb; // Process Control Block for managing this process at the kernel level
    private OS.Priority priority; // Priority of the process
    private int[] deviceIDs = new int[10];
	

    public UserlandProcess(OS.Priority priority) {
        this.thread = new Thread(this);
        this.semaphore = new Semaphore(0);
        this.quantumExpired = false;
        this.priority = priority;
        this.pcb = new PCB(this);
        Arrays.fill(deviceIDs, -1);
    }
   
public int[] getDeviceIDs() {
	return deviceIDs;
}
    public void requestStop() {
        quantumExpired = true;
    }

    public abstract void main();

    public boolean isStopped() {
        return semaphore.availablePermits() == 0;
    }

    public boolean isDone() {
        return !thread.isAlive();
    }

    public void start() {
        semaphore.release(); // Incrementing the semaphore, allowing the thread to run
        thread.start();
    }

    public void stop() {
   try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    }
    

    @Override
    public void run() {
        try {
            semaphore.acquire();
            main();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void cooperate() {
        if (quantumExpired) {
            quantumExpired = false;
            OS.switchProcess(); 
        }
    }
    
    // Get the process's priority
    public OS.Priority getPriority() {
        return priority;
    }

    // Get the PCB
    public PCB getPCB() {
        return pcb;
    }
        
    public static class Ping extends UserlandProcess {
		public Ping(Priority priority) {
			super(priority);
			// TODO Auto-generated constructor stub
		}

		@Override
        public void run() {
            int pongPid = OS.GetPidByName("Pong");
            int myPid = OS.GetPid();

            if (pongPid == -1) {
                System.out.println("PING: Pong process not found.");
                return;
            }

            System.out.println("I am PING, pong = " + pongPid);

            int what = 0;
            while (true) {
                KernelMessage messageToSend = new KernelMessage(myPid, pongPid, what, null);
                OS.SendMessage(messageToSend);

                KernelMessage receivedMessage = OS.WaitForMessage();
                System.out.println("PING: from: " + receivedMessage.senderPid + " to: " + receivedMessage.getTargetPid() + " what: " + receivedMessage.what);

                what++;
            }
        }

		@Override
		public void main() {
			// TODO Auto-generated method stub
			
		}
    }

    // Pong process
    public static class Pong extends UserlandProcess {
        public Pong(Priority priority) {
			super(priority);
			// TODO Auto-generated constructor stub
		}

		@Override
        public void run() {
            int pingPid = OS.GetPidByName("Ping");
            int myPid = OS.GetPid();

            if (pingPid == -1) {
                System.out.println("PONG: Ping process not found.");
                return;
            }

            System.out.println("I am PONG, ping = " + pingPid);

            while (true) {
                KernelMessage receivedMessage = OS.WaitForMessage();
                System.out.println("PONG: from: " + receivedMessage.senderPid + " to: " + receivedMessage.getTargetPid() + " what: " + receivedMessage.what);

                int what = receivedMessage.what + 1;
                KernelMessage messageToSend = new KernelMessage(myPid, pingPid, what, null);
                OS.SendMessage(messageToSend);
            }
        }

		@Override
		public void main() {
			// TODO Auto-generated method stub
			
		}

		
    }

}