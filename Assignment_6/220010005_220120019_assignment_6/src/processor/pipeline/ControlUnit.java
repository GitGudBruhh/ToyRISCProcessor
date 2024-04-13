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

    public ControlSignals createControlSignals(int instruction) {
        int opcode = this.extractOperationFromInstruction(instruction);

        ControlSignals controlSignals = new ControlSignals();

        if(opcode <= 22) {
            controlSignals.setControlSignal(ControlSignals.OperationSignals.WB.ordinal(), true);
        }

        if(
        (opcode <= 21 && (opcode % 2) == 1) ||
        (opcode >= 22 && opcode <= 28)
        ) {
            controlSignals.setControlSignal(ControlSignals.OperationSignals.IMMEDIATE.ordinal(), true);
        }

        switch(opcode) {
            case 0:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ADD.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 1:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ADD.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 2:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.SUB.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 3:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.SUB.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 4:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.MUL.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 5:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.MUL.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 6:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.DIV.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 7:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.DIV.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 8:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.AND.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 9:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.AND.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 10:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.OR.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 11:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.OR.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 12:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.XOR.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 13:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.XOR.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 14:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.SLT.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 15:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.SLT.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 16:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.SLL.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 17:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.SLL.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 18:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.SRL.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 19:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.SRL.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 20:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.SRA.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 21:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.SRA.ordinal(), true);
                controlSignals.setControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 22:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.LOAD.ordinal(), true);
                break;
            case 23:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.STORE.ordinal(), true);
                break;
            case 24:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.JMP.ordinal(), true);
                break;
            case 25:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.BEQ.ordinal(), true);
                break;
            case 26:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.BNE.ordinal(), true);
                break;
            case 27:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.BLT.ordinal(), true);
                break;
            case 28:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.BGT.ordinal(), true);
                break;
            case 29:
                controlSignals.setControlSignal(ControlSignals.OperationSignals.END.ordinal(), true);
                break;
            default:
                return controlSignals;
        }

        return controlSignals;
    }

    private int extractOperationFromInstruction(int instruction) {
        return (instruction >>> 27);
    }
}