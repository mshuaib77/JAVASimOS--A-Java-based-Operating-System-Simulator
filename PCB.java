package userlandprocess;


public class PCB {
private static int nextPid = 0;
private int pid;
private UserlandProcess process;
private Thread thread;

public PCB(UserlandProcess up) {
  this.process = up;
  this.pid = nextPid++;
}

public void stop() {
  process.stop();
  while(!process.isStopped()) {
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {}
  }
}

public boolean isDone() {
  return process.isDone();
}

public int getPid()
{
	return pid;
}

public void run() {
  process.start();
}

}
