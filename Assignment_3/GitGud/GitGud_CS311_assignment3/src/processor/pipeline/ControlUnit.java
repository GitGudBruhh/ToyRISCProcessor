package processor.pipeline;

public class ControlUnit {

    // public enum OperationType {add, addi, sub, subi, mul, muli, div, divi, and, andi, or, ori, xor, xori, slt, slti, sll, slli, srl, srli, sra, srai, load, store, jmp, beq, bne, blt, bgt, end}

    // private const int operationFetchHelperNumber = 31 <<< 27 //1111100....0

    /*
    Where are the signals used?
    IF: isBranchTaken

    OF: isSt, isImmediate, !! isWb(pass) !!

    EX: isAdd, isSub, isMul, isDiv, isMod, isAnd, isOr, isXor, isSlt, isSll, isSrl, isSra
    EX: isBeq, isBgt, isJmp (isUbranch replace), !! isBranchTaken (passback) !!

    MA: isLd, isSt

    RW: isLd, isJmp (isCall replace), !! isWb (passback) !!
    */

    public ControlSignals getControlSignals(int instruction) {
        int opcode = this.extractOperationFromInstruction(instruction);

        ControlSignals controlSignals = new ControlSignals();

        if(opcode <= 22) {
            controlSignals.setControlSignal(ControlSignals.OperationSignals.Wb.ordinal(), true);
        }

        if(
        (opcode <= 21 && (opcode % 2) == 1) ||
        (opcode >= 22 && opcode <= 28)
        ) {
            controlSignals.setControlSignal(ControlSignals.OperationSignals.Immediate.ordinal(), true);
        }

        switch(opcode) {
            case 0:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Add.ordinal(), true);
                break;
            case 1:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Add.ordinal(), true);
                break;
            case 2:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Sub.ordinal(), true);
                break;
            case 3:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Sub.ordinal(), true);
                break;
            case 4:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Mul.ordinal(), true);
                break;
            case 5:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Mul.ordinal(), true);
                break;
            case 6:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Div.ordinal(), true);
                break;
            case 7:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Div.ordinal(), true);
                break;
            case 8:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.And.ordinal(), true);
                break;
            case 9:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.And.ordinal(), true);
                break;
            case 10:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Or.ordinal(), true);
                break;
            case 11:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Or.ordinal(), true);
                break;
            case 12:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Xor.ordinal(), true);
                break;
            case 13:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Xor.ordinal(), true);
                break;
            case 14:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Slt.ordinal(), true);
                break;
            case 15:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Slt.ordinal(), true);
                break;
            case 16:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Sll.ordinal(), true);
                break;
            case 17:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Sll.ordinal(), true);
                break;
            case 18:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Srl.ordinal(), true);
                break;
            case 19:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Srl.ordinal(), true);
                break;
            case 20:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Sra.ordinal(), true);
                break;
            case 21:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Sra.ordinal(), true);
                break;
            case 22:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Load.ordinal(), true);
                break;
            case 23:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Store.ordinal(), true);
                break;
            case 24:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Jmp.ordinal(), true);
                break;
            case 25:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Beq.ordinal(), true);
                break;
            case 26:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Bne.ordinal(), true);
                break;
            case 27:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Blt.ordinal(), true);
                break;
            case 28:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.Bgt.ordinal(), true);
                break;
            case 29:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.End.ordinal(), true);
                break;
        }

        return controlSignals;
    }

    private int extractOperationFromInstruction(int instruction) {
        return (instruction >>> 27);
    }
}