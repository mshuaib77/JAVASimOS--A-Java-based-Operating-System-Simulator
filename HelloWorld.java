package userlandprocess;


public class HelloWorld extends UserlandProcess {
	

    public HelloWorld(OS.Priority priority) {
		super(priority);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void main() {
		while (true) {
            System.out.println("Hello World");
            cooperate();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}