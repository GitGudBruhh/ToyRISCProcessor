package processor.pipeline;

public class ControlSignals {

    public enum OperationSignals {Add, Sub, Mul, Div, And, Or, Xor, Slt ,Sll, Srl, Sra, Load, Store, Jmp, Beq, Bne, Blt, Bgt, End, Immediate, Wb, BranchTaken}

    boolean arrayOfSignals[22];

    ControlSignals() {
        for(int i = 0; i < 22; i++)
            this.arrayOfSignals[i] = false;
    }
    public void setControlSignal(int index, boolean cSig) {
        this.arrayOfSignals[idx] == cSig;
    }

    public boolean getControlSignal() {
        return this.arrayOfSignals[idx];
    }
`
    public boolean[] getAllControlSignals() {
        return this.arrayOfSignals;
    }
}