package processor.pipeline;

// import java.lang.Integer;

import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	Interlocks interlocks;
	
	public Execute(Processor containingProcessor,
	OF_EX_LatchType oF_EX_Latch,
	EX_MA_LatchType eX_MA_Latch,
	EX_IF_LatchType eX_IF_Latch,
	IF_OF_LatchType iF_OF_Latch,
	Interlocks interlocks) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch; // USED ONLY FOR TURNING ON THE STAGE
        this.interlocks = interlocks;

	}
	
	public void performEX() {
		ControlSignals controlSignals = OF_EX_Latch.getControlSignals();

		/*
        ===================================================================================================
        Stall if control signals have an IGNORE signal
        ===================================================================================================
        */
        // if(controlSignals.getMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal())) {
        //     OF_EX_Latch.setEX_enable(false);
        //     return;
        // }

		if(OF_EX_Latch.isEX_enable()) {
            if(!controlSignals.getOperationSignal(ControlSignals.OperationSignals.END.ordinal())) {

                int currentPC = OF_EX_Latch.getPc();
                int branchTarget = OF_EX_Latch.getBranchTarget();
                int B = OF_EX_Latch.getB();
                int A = OF_EX_Latch.getA();
                int op2 = OF_EX_Latch.getOp2();
                int instruction = OF_EX_Latch.getInstruction();

                long remainder;

                int branchPC = currentPC + 1;
                long aluResult = 0;
                int aluResultLogical;

                /*
                IMPORTANT NOTE:
                The aluResult is a 64 bit integer, with the most significant 32 bits containing information to be placed in the x31 register
                (in certain cases).

                If any arithmetic operation result crosses the 4-byte limit, the structure of the long aluResult automatically
                takes care of the overflow.

                Logical operation results must always be limited to the least significant 32 bits (to be placed in the destination register).
                Therefore, a bitwise and operation is performed that sets all the most significant 32 bits to 0 (will come in handy in RW).

                For the division operation, the remainder is set in the most significant 32 bits by performing a bitwise or operation.

                In the RW stage, we check for an overflow by comparing the most significant 32 bits with 0000...00 or 1111...11.
                The 0s check if the aluResult is a non negative integer with magnitude less than 2^32.
                The 0s check if the aluResult is a negative integer with magnitude less than 2^32.

                However, if the control signals specify that a division operation has taken place, the remainder will be
                forcefully written onto x31 even if it is equal to zero.
                */

                if (
                controlSignals.getOperationSignal(ControlSignals.OperationSignals.JMP.ordinal()) ||
                controlSignals.getOperationSignal(ControlSignals.OperationSignals.BEQ.ordinal()) ||
                controlSignals.getOperationSignal(ControlSignals.OperationSignals.BNE.ordinal()) ||
                controlSignals.getOperationSignal(ControlSignals.OperationSignals.BLT.ordinal()) ||
                controlSignals.getOperationSignal(ControlSignals.OperationSignals.BGT.ordinal())
                ) {
                    branchPC = branchTarget;
                }

                int opcode = instruction >>> 27;
                switch(opcode) {
                    case 0:
                        aluResult = (long) A + (long) B;
                        break;
                    case 1:
                        aluResult = (long) A + (long) B;
                        break;
                    case 2:
                        aluResult = (long) A - (long) B;
                        break;
                    case 3:
                        aluResult = (long) A - (long) B;
                        break;
                    case 4:
                        aluResult = (long) A * (long) B;
                        break;
                    case 5:
                        aluResult = (long) A * (long) B;
                        break;
                    case 6:
                        aluResult = (long) A / (long) B;
                        remainder = (long) A % (long) B;
                        aluResult = (remainder << 32) | aluResult;
                        break;
                    case 7:
                        aluResult = (long) A / (long) B;
                        remainder = (long) A % (long) B;
                        aluResult = (remainder << 32) | aluResult;
                        break;
                    case 8:
                        aluResultLogical = A & B;
                        aluResult = (long) aluResultLogical & 0x00000000ffffffffL;
                        break;
                    case 9:
                        aluResultLogical = A & B;
                        aluResult = (long) aluResultLogical & 0x00000000ffffffffL;
                        break;
                    case 10:
                        aluResultLogical = A | B;
                        aluResult = (long) aluResultLogical & 0x00000000ffffffffL;
                        break;
                    case 11:
                        aluResultLogical = A | B;
                        aluResult = (long) aluResultLogical & 0x00000000ffffffffL;
                        break;
                    case 12:
                        aluResultLogical = (A & (~B)) | (B & (~A));
                        aluResult = (long) aluResultLogical & 0x00000000ffffffffL;
                        break;
                    case 13:
                        aluResultLogical = (A & (~B)) | (B & (~A));
                        aluResult = (long) aluResultLogical & 0x00000000ffffffffL;
                        break;
                    case 14:
                        aluResult = (A < B) ? 1 : 0;
                        break;
                    case 15:
                        aluResult = (A < B) ? 1 : 0;
                        break;
                    case 16:
                        aluResult = ((long) A << (long) B);
                        break;
                    case 17:
                        aluResult = ((long) A << (long) B);
                        break;
                    case 18:
                        aluResult = ((long) A >>> (long) B);
                        break;
                    case 19:
                        aluResult = ((long) A >>> (long) B);
                        break;
                    case 20:
                        aluResult = ((long) A >> (long) B);
                        break;
                    case 21:
                        aluResult = ((long) A >> (long) B);
                        break;
                    case 22:
                        aluResult = ((long) A + (long) B); //load
                        break;
                    case 23:
                        aluResult = ((long) A + (long) B); //store
                        break;
                    case 24:
                        branchPC = branchTarget;
                        controlSignals.setMiscSignal(ControlSignals.MiscSignals.BRANCHTAKEN.ordinal(), true);
                        ControlInterlock cI_buffer = interlocks.getControlInterlockBuf();
                        cI_buffer.setBranchTaken(true);
                        interlocks.setControlInterlockBuf(cI_buffer);

                        break;
                    case 25:
                        branchPC = branchTarget;
                        if (A == B) {
                            controlSignals.setMiscSignal(ControlSignals.MiscSignals.BRANCHTAKEN.ordinal(), true);
                            ControlInterlock cI_buffer = interlocks.getControlInterlockBuf();
                            cI_buffer.setBranchTaken(true);
                            interlocks.setControlInterlockBuf(cI_buffer);
                        }
                        break;
                    case 26:
                        branchPC = branchTarget;
                        if (A != B) {
                            controlSignals.setMiscSignal(ControlSignals.MiscSignals.BRANCHTAKEN.ordinal(), true);
                            ControlInterlock cI_buffer = interlocks.getControlInterlockBuf();
                            cI_buffer.setBranchTaken(true);
                            interlocks.setControlInterlockBuf(cI_buffer);
                        }
                        break;
                    case 27:
                        branchPC = branchTarget;
                        if (A < B) {
                            controlSignals.setMiscSignal(ControlSignals.MiscSignals.BRANCHTAKEN.ordinal(), true);
                            ControlInterlock cI_buffer = interlocks.getControlInterlockBuf();
                            cI_buffer.setBranchTaken(true);
                            interlocks.setControlInterlockBuf(cI_buffer);
                        }
                        break;
                    case 28:
                        branchPC = branchTarget;
                        if (A > B) {
                            controlSignals.setMiscSignal(ControlSignals.MiscSignals.BRANCHTAKEN.ordinal(), true);
                            ControlInterlock cI_buffer = interlocks.getControlInterlockBuf();
                            cI_buffer.setBranchTaken(true);
                            interlocks.setControlInterlockBuf(cI_buffer);
                        }
                        break;
                    case 29:
                        controlSignals.setOperationSignal(ControlSignals.OperationSignals.END.ordinal(), true);
                        break;
                    default:
                        return;
                }

                EX_IF_Latch.setBranchPC(branchPC);
                EX_MA_Latch.setInstruction(instruction);
                EX_MA_Latch.setPc(currentPC);
                EX_MA_Latch.setAluResult(aluResult);
                EX_MA_Latch.setOp2(op2);
            }

            /*
            Emptying the latch when an end instruction passes through.
            */
            else {
                EX_IF_Latch.setBranchPC(null);
                EX_MA_Latch.setInstruction(null);
                EX_MA_Latch.setPc(null);
                EX_MA_Latch.setAluResult(null);
                EX_MA_Latch.setOp2(null);
            }

            EX_MA_Latch.setControlSignals(controlSignals);
            EX_IF_Latch.setControlSignals(controlSignals); //WARNING: USE ONLY BRANCHTAKEN SIGNAL
            OF_EX_Latch.setEX_enable(false);
            // EX_MA_Latch.setMA_enable(true);
            IF_OF_Latch.setOF_enable(true);
        }
	}
}