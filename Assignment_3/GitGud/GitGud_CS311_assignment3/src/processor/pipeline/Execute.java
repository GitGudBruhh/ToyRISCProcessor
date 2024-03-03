package processor.pipeline;

// import java.lang.Integer;

import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX() {
		//TODO
		ControlSignals controlSignals = OF_EX_Latch.getControlSignals();

		if(OF_EX_Latch.isEX_enable()) {
            if(!controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {
                int currentPC = OF_EX_Latch.getPc();
                int branchTarget = OF_EX_Latch.getBranchTarget();
                int B = OF_EX_Latch.getB();
                int A = OF_EX_Latch.getA();
                int op2 = OF_EX_Latch.getOp2();
                int instruction = OF_EX_Latch.getInstruction();

                        System.out.println("INSTRUCTION OPERATION");
                        System.out.println(instruction >>> 27);
                        System.out.println("CSIG BEFORE EX");
                        controlSignals.display();

                long remainder;

                int branchPC = currentPC + 1;
                long aluResult = 0;
                int aluResultLogical;

                if (
                controlSignals.getControlSignal(ControlSignals.OperationSignals.JMP.ordinal()) ||
                controlSignals.getControlSignal(ControlSignals.OperationSignals.BEQ.ordinal()) ||
                controlSignals.getControlSignal(ControlSignals.OperationSignals.BNE.ordinal()) ||
                controlSignals.getControlSignal(ControlSignals.OperationSignals.BLT.ordinal()) ||
                controlSignals.getControlSignal(ControlSignals.OperationSignals.BGT.ordinal())
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
                        aluResult = (remainder << 32) & aluResult;
                        break;
                    case 7:
                        aluResult = (long) A / (long) B;
                        remainder = (long) A % (long) B;
                        aluResult = (remainder << 32) & aluResult;
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
                        controlSignals.setControlSignal(ControlSignals.OperationSignals.BRANCHTAKEN.ordinal(), true);
                        break;
                    case 25:
                        branchPC = branchTarget;
                        if (A == B)
                            controlSignals.setControlSignal(ControlSignals.OperationSignals.BRANCHTAKEN.ordinal(), true);
                        break;
                    case 26:
                        branchPC = branchTarget;
                        if (A != B)
                            controlSignals.setControlSignal(ControlSignals.OperationSignals.BRANCHTAKEN.ordinal(), true);
                        break;
                    case 27:
                        branchPC = branchTarget;
                        if (A < B)
                            controlSignals.setControlSignal(ControlSignals.OperationSignals.BRANCHTAKEN.ordinal(), true);
                        break;
                    case 28:
                        branchPC = branchTarget;
                        if (A > B)
                            controlSignals.setControlSignal(ControlSignals.OperationSignals.BRANCHTAKEN.ordinal(), true);
                            break;
                    case 29:
                        controlSignals.setControlSignal(ControlSignals.OperationSignals.END.ordinal(), true);
                        break;
                    default:
                        return;
                }

                EX_IF_Latch.setBranchPC(branchPC);
                EX_IF_Latch.setControlSignals(controlSignals);

                EX_MA_Latch.setPc(currentPC);
                EX_MA_Latch.setAluResult(aluResult);
                EX_MA_Latch.setOp2(op2);
                EX_MA_Latch.setInstruction(instruction);
            }
            System.out.println("CSIG AFTER EX");
            controlSignals.display();
            System.out.println();

            EX_MA_Latch.setControlSignals(controlSignals);
            OF_EX_Latch.setEX_enable(false);
            EX_MA_Latch.setMA_enable(true);
        }
	}
}
