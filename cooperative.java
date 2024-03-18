package userlandprocess;


public class cooperative extends UserlandProcess {
	 public cooperative() {
	        super(OS.Priority.MEDIUM); // Assuming it starts with Interactive priority
	    }

	    @Override
	    public void main() {
	        for (int i = 0; i < 5; i++) {
	            System.out.println("Cooperative process is working...");
	            cooperate(); // Yield to the scheduler
	            OS.Sleep(1000); // Sleep for 1 second
	        }
	        System.out.println("Cooperative process completed.");
	    }
	}

