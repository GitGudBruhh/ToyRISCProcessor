package processor.pipeline;

public class DataInterlock {
    boolean[] regLockVector;
    // boolean isStalled;

    public DataInterlock() {
        this.regLockVector = new boolean[32];

        for(boolean elem : regLockVector)
            elem = false;

        // this.isStalled = false;
    }

    public boolean isRegisterLocked(int registerNo) {
        return this.regLockVector[registerNo];
    }

    public void setRegisterLock(int registerNo, boolean lock) {
        this.regLockVector[registerNo] = lock;
    }

    // public boolean isStalled() {
    //     return this.isStalled;
    // }
    //
    // public void setStalled(boolean stalled) {
    //     this.isStalled = stalled;
    // }
}