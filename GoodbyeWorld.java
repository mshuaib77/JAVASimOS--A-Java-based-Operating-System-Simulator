package userlandprocess;

public class GoodbyeWorld extends UserlandProcess {
	
	public GoodbyeWorld(OS.Priority priority) {
		super(priority);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void main() {
		while (true) {
            System.out.println("Goodbye World");
            cooperate();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}