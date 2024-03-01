package processor.pipeline;

public class ControlSignals {

    public enum OperationSignals {Add, Sub, Mul, Div, And, Or, Xor, Slt ,Sll, Srl, Sra, Load, Store, Jmp, Beq, Bne, Blt, Bgt, End, Immediate, Wb, BranchTaken}

    boolean[] arrayOfSignals;

    ControlSignals() {
        this.arrayOfSignals = new boolean[22];
        for(int i = 0; i < 22; i++)
            this.arrayOfSignals[i] = false;
    }

    public void setControlSignal(int index, boolean cSig) {
        this.arrayOfSignals[index] = cSig;
    }

    public boolean getControlSignal(int index) {
        return this.arrayOfSignals[index];
    }

    public boolean[] getAllControlSignals() {
        return this.arrayOfSignals;
    }
}