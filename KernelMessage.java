package userlandprocess;

public class KernelMessage {
	
	int senderPid;
    private int targetPid;
    int what;
    private byte[] data;

    public KernelMessage(int senderPid, int targetPid, int what, byte[] data) {
        this.senderPid = senderPid;
        this.targetPid = targetPid;
        this.what = what;
        this.data = data;
    }

    public KernelMessage(KernelMessage km) {
        this.senderPid = km.senderPid;
        this.targetPid = km.targetPid;
        this.what = km.what;
        this.data = km.data;
    }

    @Override
    public String toString() {
        return "From: " + senderPid + " To: " + targetPid + " What: " + what;
    }

    public Integer getTargetPid() {
        return targetPid;
    }
}
