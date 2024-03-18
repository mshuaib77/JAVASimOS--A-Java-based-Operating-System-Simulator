package userlandprocess;


public class HighPriority extends UserlandProcess {
	  public HighPriority() {
	        super(OS.Priority.HIGH);
	    }

	    @Override
	    public void main() {
	        long endTime = System.currentTimeMillis() + 10000; // Run for 10 seconds
	        while (System.currentTimeMillis() < endTime) {
	            // Simulate work by just looping
	            cooperate(); // Check if it needs to yield back to the scheduler
	        }
	        System.out.println("Long-running real-time process completed.");
	    }

}
