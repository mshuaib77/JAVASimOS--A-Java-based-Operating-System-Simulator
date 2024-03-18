package userlandprocess;

import java.util.LinkedList;

import userlandprocess.OS.Priority;

public class KernelandProcess {
	UserlandProcess userlandProcess;
    Priority currentPriority;
    static int nextpid = 0;
    int pid;
    private boolean started;
    private Thread thread;
    private String name;
    private LinkedList<KernelMessage> messageQueue = new LinkedList<>();

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMessage(KernelMessage message) {
        messageQueue.add(message);
    }

    public KernelMessage getMessage() {
        if (!messageQueue.isEmpty()) {
            return messageQueue.poll(); // Retrieves and removes the message
        }
        return null;
    }

    public Integer getPid() {
        return pid;
    }

}
