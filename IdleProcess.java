package userlandprocess;


public class IdleProcess extends UserlandProcess {
	
	 public IdleProcess() {
	        // Call the parent constructor with the lowest priority
	        super(OS.Priority.LOW);
	    }
	@Override
	public void main() {
        while (true) {
            cooperate();
                try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
    }
}