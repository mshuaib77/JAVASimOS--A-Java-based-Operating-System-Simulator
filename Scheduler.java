package userlandprocess;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.*;

@SuppressWarnings("unused")
public class Scheduler {

private LinkedList<UserlandProcess> processes;
//private List<SleepingProcess> sleeping;
private static List<KernelandProcess> realtimeQueue = Collections.synchronizedList(new LinkedList<>());
private static List<KernelandProcess> interactiveQueue = Collections.synchronizedList(new LinkedList<>());
private static List<KernelandProcess> backgroundQueue = Collections.synchronizedList(new LinkedList<>());
private static List<KernelandProcess> sleepingProcesses = Collections.synchronizedList(new LinkedList<>());


private final ArrayList<Queue<UserlandProcess>> priorityQueues;
private final PriorityQueue<SleepingProcess> sleepQueue;
private final Clock clock;
private Timer timer;
public static UserlandProcess currentlyRunning;
private Kernel kernel;

private int nextPid = 1;


public Scheduler() {
 this.priorityQueues = new ArrayList<>(OS.Priority.values().length);
      for (int i = 0; i < OS.Priority.values().length; i++) {
          this.priorityQueues.add(new ConcurrentLinkedQueue<>());
      }
   this.sleepQueue = new PriorityQueue<>(Comparator.comparingLong(SleepingProcess::getWakeUpTime));
    this.clock = Clock.systemDefaultZone();
}
      public static class SleepingProcess {
    		 private final UserlandProcess process;
    	     private final long wakeUpTime;

    	     public SleepingProcess(UserlandProcess process, long wakeUpTime) {
    	         this.process = process;
    	         this.wakeUpTime = wakeUpTime;
    	     }

    	     public UserlandProcess getProcess() {
    	         return process;
    	     }

    	     public long getWakeUpTime() {
    	         return wakeUpTime;
    	     
    	  }
}

    public int createProcess(UserlandProcess up, OS.Priority priority) {
    PCB pcb = new PCB(up); 
   int pid = nextPid++;
    priorityQueues.get(priority.ordinal()).add(up);
    if (currentlyRunning == null) {
      switchProcess();
    }
    return pid;
  }

public void switchProcess() {
	  wakeUpProcesses();
    
      for (Queue<UserlandProcess> queue : priorityQueues) {
          UserlandProcess nextProcess = queue.poll();
          if (nextProcess != null) {
              if (currentlyRunning != null && !currentlyRunning.isDone()) {
                  priorityQueues.get(currentlyRunning.getPriority().ordinal()).add(currentlyRunning); 
              }
              currentlyRunning = nextProcess;
              currentlyRunning.start(); // Start next process
              return;
          }
      }
}

//Returns the currently running process
public UserlandProcess getCurrentlyRunning() {
    return currentlyRunning;
}

private void wakeUpProcesses() {
    long currentTime = clock.millis();
    while (!sleepQueue.isEmpty() && sleepQueue.peek().getWakeUpTime() <= currentTime) {
        UserlandProcess toWake = sleepQueue.poll().getProcess();
        priorityQueues.get(toWake.getPriority().ordinal()).add(toWake); 
    }
}

  public void sleep(int milliseconds) {
	  if(currentlyRunning != null){
		  long wakeUpTime = clock.millis() + milliseconds;
          sleepQueue.add(new SleepingProcess(currentlyRunning, wakeUpTime));  
    switchProcess();
  }
  }
  
public static int getPid() {
	return currentlyRunning.pid;	
}

public static void addProcess(KernelandProcess process) {
    switch (process.currentPriority) {
    case HIGH:
        realtimeQueue.add(process);
        break;
    case MEDIUM:
        interactiveQueue.add(process);
        break;
    case LOW:
        backgroundQueue.add(process);
        break;
    }
}
    

public static void removeProcess(KernelandProcess process) {
        switch (process.currentPriority) {
            case HIGH:
                realtimeQueue.remove(process);
                break;
            case MEDIUM:
                interactiveQueue.remove(process);
                break;
            case LOW:
                backgroundQueue.remove(process);
                break;
        }
}
}