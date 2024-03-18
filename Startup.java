package userlandprocess;


import java.util.Timer;
import java.util.TimerTask;

public class Startup {
	public static void main(String[] args) {

	    HighPriority timeCriticalProcess = new   HighPriority();
	    
	    cooperative nonTimeCriticalProcess = new  cooperative();

	    timeCriticalProcess.start();
	    
	    nonTimeCriticalProcess.start();

	    OS.Startup(new IdleProcess()); // Set up the idle process

	    // Create a time-critical real-time process
	    
	    OS.CreateProcess(timeCriticalProcess, OS.Priority.HIGH); 

	    // Create a non-time-critical cooperative process

	    OS.CreateProcess(nonTimeCriticalProcess, OS.Priority.MEDIUM);

	    Timer timer = new Timer(true);
	    
	    timer.scheduleAtFixedRate(new TimerTask() {
	       
	        public void run() 
	        {
	            OS.switchProcess();  
	        }
	    
	    }, 0, 250);

	}

	}