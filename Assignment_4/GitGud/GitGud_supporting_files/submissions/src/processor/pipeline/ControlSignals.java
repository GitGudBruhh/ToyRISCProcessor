package processor.pipeline;

public class ControlSignals {

    public enum OperationSignals {ADD, SUB, MUL, DIV, AND, OR, XOR, SLT, SLL, SRL, SRA, LOAD, STORE, JMP, BEQ, BNE, BLT, BGT, END, ALUSIGNAL, IMMEDIATE, WB, BRANCHTAKEN}

    boolean[] arrayOfSignals;

    public ControlSignals() {
        this.arrayOfSignals = new boolean[23];
        for(int i = 0; i < 23; i++)
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

    public void display() {
        for (OperationSignals v : OperationSignals.values()) {
            if(arrayOfSignals[v.ordinal()]) {
                System.out.print(v);
                System.out.print(" " + arrayOfSignals[v.ordinal()] + ", ");
            }
        }

        System.out.println();
    }
}