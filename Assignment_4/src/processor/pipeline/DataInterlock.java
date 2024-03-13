package processor.pipeline;

public class DataInterlock {
    boolean[] lockVector;
    boolean isStalled;

    DataInterlock() {
        this.lockVector = new boolean[32];
        for(elem : lockVector)
            elem = false;
        this.isStalled = false;
    }

    public boolean isRegisterLocked(int registerNo) {
        return this.lockVector[registerNo];
    }

    public void setRegisterLock(int registerNo, boolean lock) {
        this.lockVector[registerNo] = lock;
    }

    public boolean isStalled(int registerNo) {
        return this.isStalled;
    }

    public void setStalled(int registerNo, boolean stalled) {
        this.isStalled = stalled;
    }
}