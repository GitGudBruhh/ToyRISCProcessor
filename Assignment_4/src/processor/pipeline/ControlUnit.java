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
            controlSignals.setMiscSignal(ControlSignals.MiscSignals.WB.ordinal(), true);
        }

        if(
        (opcode <= 21 && (opcode % 2) == 1) ||
        (opcode >= 22 && opcode <= 28)
        ) {
            controlSignals.setMiscSignal(ControlSignals.MiscSignals.IMMEDIATE.ordinal(), true);
        }

        switch(opcode) {
            case 0:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.ADD.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 1:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.ADD.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 2:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.SUB.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 3:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.SUB.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 4:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.MUL.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 5:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.MUL.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 6:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.DIV.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 7:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.DIV.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 8:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.AND.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 9:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.AND.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 10:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.OR.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 11:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.OR.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 12:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.XOR.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 13:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.XOR.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 14:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.SLT.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 15:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.SLT.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 16:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.SLL.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 17:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.SLL.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 18:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.SRL.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 19:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.SRL.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 20:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.SRA.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 21:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.SRA.ordinal(), true);
                controlSignals.setMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal(), true);
                break;
            case 22:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.LOAD.ordinal(), true);
                break;
            case 23:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.STORE.ordinal(), true);
                break;
            case 24:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.JMP.ordinal(), true);
                break;
            case 25:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.BEQ.ordinal(), true);
                break;
            case 26:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.BNE.ordinal(), true);
                break;
            case 27:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.BLT.ordinal(), true);
                break;
            case 28:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.BGT.ordinal(), true);
                break;
            case 29:
                controlSignals.setOperationSignal(ControlSignals.OperationSignals.END.ordinal(), true);
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