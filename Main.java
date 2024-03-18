package userlandprocess;
import java.util.Timer;
import java.util.TimerTask;

import userlandprocess.OS.Priority;

//import OS.Priority;

public class Main {

	  public static void main(String[] args) {

	      OS.CreateProcess(new UserlandProcess.Ping(null), Priority.LOW);
	      OS.CreateProcess(new GoodbyeWorld(null), Priority.HIGH);
	        OS.CreateProcess(new UserlandProcess.Pong(null), Priority.LOW);


		HelloWorld hw = new HelloWorld(null);
		GoodbyeWorld gw = new GoodbyeWorld(null);
		
		hw.start();
		gw.start();
		
	    OS.Startup(hw);
	    OS.CreateProcess(gw, null);
	    
	    Timer timer = new Timer(true);
	    timer.scheduleAtFixedRate(new TimerTask() {
	    	
	    	public void run() {
	    		OS.switchProcess();
	    	}
	    },0, 100);
	    
	  }

	}
