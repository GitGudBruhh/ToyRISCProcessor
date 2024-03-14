package processor.pipeline;

public class ControlSignals {

    public enum OperationSignals {ADD, SUB, MUL, DIV, AND, OR, XOR, SLT, SLL, SRL, SRA, LOAD, STORE, JMP, BEQ, BNE, BLT, BGT, END}
    public enum MiscSignals {ALUSIGNAL, IMMEDIATE, WB, BRANCHTAKEN, IGNORE}

    boolean[] opSignals;
    boolean[] miscSignals;

    ControlSignals() {
        this.opSignals = new boolean[23];
        for(int i = 0; i < 23; i++)
            this.opSignals[i] = false;

        this.miscSignals = new boolean[5];
        for(int i = 0; i < 5; i++)
            this.miscSignals[i] = false;
    }

    public void setOperationSignal(int index, boolean cSig) {
        this.opSignals[index] = cSig;
    }

    public boolean getOperationSignal(int index) {
        return this.opSignals[index];
    }

    public void setMiscSignal(int index, boolean cSig) {
        this.miscSignals[index] = cSig;
    }

    public boolean getMiscSignal(int index) {
        return this.miscSignals[index];
    }

    // public boolean[] getAllControlSignals() {
    //     return this.arrayOfSignals;
    // }

    public void display() {
        for (OperationSignals v : OperationSignals.values()) {
            if(opSignals[v.ordinal()]) {
                System.out.print(v);
                System.out.print(" " + opSignals[v.ordinal()] + ", ");
            }
        }

        for (MiscSignals v : MiscSignals.values()) {
            if(miscSignals[v.ordinal()]) {
                System.out.print(v);
                System.out.print(" " + miscSignals[v.ordinal()] + ", ");
            }
        }

        System.out.println();
    }
}