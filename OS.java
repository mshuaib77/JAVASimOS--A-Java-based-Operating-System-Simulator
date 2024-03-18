package userlandprocess;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class OS {
	private static Kernel kernel;
	private static Map<Integer, KernelandProcess> processMap = new HashMap<>();
	private static LinkedList<KernelandProcess> waitingProcesses =new LinkedList<>();
    static CallType currentCall; // The current call type
    static ArrayList<Object> parameters = new ArrayList<>(); // Parameters for the call
    public static Scheduler scheduler = new Scheduler();
    
    static Object returnValue; // Return value from the kernel

    enum CallType {
        CreateProcess,
        SwitchProcess, Sleep,
       
    }
    
    enum Priority {
        HIGH, MEDIUM, LOW;
    }
    
    public static int CreateProcess(UserlandProcess up, Priority priority ) {
    	 resetParameters();
    	   parameters.add(up);
           parameters.add(priority);
           currentCall = CallType.CreateProcess;
           switchToKernel();
           return (int) returnValue;
    }

    // Initializes the OS with the kernel and the initial process
    public static void Startup(UserlandProcess init) {
        resetParameters();
        // Create initial and idle processes
        CreateProcess(init, Priority.MEDIUM);
        CreateProcess(new IdleProcess(), Priority.LOW);
        kernel.start();
    }
    

    public static void switchProcess() {
    	 resetParameters();
    	currentCall = CallType.SwitchProcess;
    	kernel.run();
    }
    
    // Resets the system call parameters
    private static void resetParameters() {
        parameters.clear();
        returnValue = null;
    }

    // Switches execution to the kernel mode
    private static void switchToKernel() {
       kernel.run();  
    }
    
    public static void Sleep(int milliseconds) {
        resetParameters();
        parameters.add(milliseconds);
        currentCall = CallType.Sleep;
        kernel.run();
    	}
    
    public static int GetPid() {
        return kernel.getPid();
    }

    public static int GetPidByName(String name) {
        for (Map.Entry<Integer, KernelandProcess> entry : processMap.entrySet()) {
            if (entry.getValue().getName().equals(name)) {
                return entry.getKey();
            }
        }
        return -1; // Not found
    }

    public static void SendMessage(KernelMessage km) {
        KernelMessage copyMessage = new KernelMessage(km);
        KernelandProcess targetProcess = processMap.get(km.getTargetPid());
        if (targetProcess != null) {
            targetProcess.addMessage(copyMessage);}
    }

    public static KernelMessage WaitForMessage() {
        int currentPid = GetPid();
        KernelandProcess currentProcess = processMap.get(currentPid);
        KernelMessage message = currentProcess.getMessage();

        if (message == null) {
            descheduleProcess(currentPid);

            while (message == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message = currentProcess.getMessage();
            }
            rescheduleProcess(currentPid);
        }

        return message;
    }
    
    public static void descheduleProcess(int pid) {// Deschedules a process
        KernelandProcess process = processMap.get(pid);
        if (process != null && !waitingProcesses.contains(process)) {
            Scheduler.removeProcess(process);
            waitingProcesses.add(process);
        }
    }

    public static void rescheduleProcess(int pid) {// Reschedules a process
        KernelandProcess process = processMap.get(pid);
        if (process != null && waitingProcesses.contains(process)) {
            Scheduler.addProcess(process);
            waitingProcesses.remove(process);
        }
    }
    public void CreateProcess(KernelandProcess process) {//adds a process to the process map

        processMap.put(process.getPid(), process);
    }

	
}



