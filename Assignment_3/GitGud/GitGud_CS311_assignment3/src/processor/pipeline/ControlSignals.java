package processor.pipeline;

public class ControlSignals {

    public enum OperationSignals {ADD, SUB, MUL, DIV, AND, OR, XOR, SLT, SLL, SRL, SRA, LOAD, STORE, JMP, BEQ, BNE, BLT, BGT, END, IMMEDIATE, WB, BRANCHTAKEN}

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

    public void display() {
        for (boolean x : arrayOfSignals) {
            System.out.print(x);
            System.out.print(" ");
        }

        System.out.println();
    }
}