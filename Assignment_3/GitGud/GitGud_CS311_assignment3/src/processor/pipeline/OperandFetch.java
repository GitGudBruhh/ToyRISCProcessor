package processor.pipeline;

import processor.Processor;
// import src.generic.Simulator;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	ControlUnit controlUnit;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.controlUnit = new ControlUnit();
	}
	
	public void performOF()
	{
		int instruction = IF_OF_Latch.getInstruction();
		ControlSignals controlSignals = controlUnit.createControlSignals(instruction);
		System.out.println("BEFORE OF");
		controlSignals.display();

		// if(controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal()))
		// 	System.out.println("AAAAAA");

		// System.out.println("BRAHBRHAH");

		if(IF_OF_Latch.isOF_enable()) {
			//TODO
			if(!controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {
				int opcode = instruction >>> 27;
				int currentPC = IF_OF_Latch.getPc();


				int branchTarget;
				int immx;
				int rd;
				int rs2;
				int rs1;

				//RI and R2I types
				if (controlSignals.getControlSignal(ControlSignals.OperationSignals.IMMEDIATE.ordinal())) {
					//Unconditional Branch - RI (op2 unused)
					if (controlSignals.getControlSignal(ControlSignals.OperationSignals.JMP.ordinal())) {
						immx = (instruction << 10) >> 10;
						branchTarget = currentPC + immx;
						rd = (instruction << 5) >>> 27;
						// System.out.println("IMMX");
						// System.out.println(immx);

						if (branchTarget == 0) {
							OF_EX_Latch.setBranchTarget(this.containingProcessor.getRegisterFile().getValue(rd));
							OF_EX_Latch.setB(this.containingProcessor.getRegisterFile().getValue(rd)); //immx
							OF_EX_Latch.setA(0); //OF NO USE
							OF_EX_Latch.setOp2(0); //OF NO USE
						}

						else {
							OF_EX_Latch.setBranchTarget(branchTarget);
							OF_EX_Latch.setB(immx);
							OF_EX_Latch.setOp2(0); //OF NO USE
						}
					}

					//Conditional Branches, Arithmetic/Logical, and Load/Store Operations - R2I
					else {
						rs1 = (instruction << 5) >>> 27;
						rd = (instruction << 10) >>> 27;
						immx = (instruction << 15) >> 15;
						branchTarget = currentPC + immx;
						// System.out.println("IMMX " + immx);

						//Conditional Branches
						if (
						controlSignals.getControlSignal(ControlSignals.OperationSignals.BEQ.ordinal()) ||
						controlSignals.getControlSignal(ControlSignals.OperationSignals.BNE.ordinal()) ||
						controlSignals.getControlSignal(ControlSignals.OperationSignals.BLT.ordinal()) ||
						controlSignals.getControlSignal(ControlSignals.OperationSignals.BGT.ordinal())
						) {
							// THE MULTIPLEXER DOES NOT FOLLOW THE DIAGRAM IN THE SLIDES EVEN THOUGH
							// CONDITIONAL BRANCHES ARE OF IMMEDIATE TYPE - (NO SEPERATE COMPARE INSTRUCTION)
							// THE VALUE OF B IS FROM RD AND NOT IMMX
							// ALU COMPARES A AND B
							OF_EX_Latch.setBranchTarget(branchTarget);
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
							OF_EX_Latch.setB(this.containingProcessor.getRegisterFile().getValue(rd));
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd));
							// System.out.println("BRANCHTARGET");
							// System.out.println(branchTarget);

						}

						else if (controlSignals.getControlSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
							// RS1 AND RD ARE SWITCHED FOR THE STORE INSTRUCTION, CAREFUL HERE, SIMPLERISC AND TOYRISC
							// ARE DIFFERENT
							// rd  <- rs1 + immx
							// Reg <- A   + B
							OF_EX_Latch.setBranchTarget(branchTarget); //OF NO USE
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
							OF_EX_Latch.setB(immx);
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd)); //OF NO USE
						}

						else if (controlSignals.getControlSignal(ControlSignals.OperationSignals.STORE.ordinal())) {
							// RS1 AND RD ARE SWITCHED FOR THE STORE INSTRUCTION, CAREFUL HERE, SIMPLERISC AND TOYRISC
							// ARE DIFFERENT
							// rs1 -> rd + immx
							// Reg -> A  + B
							OF_EX_Latch.setBranchTarget(branchTarget); //OF NO USE
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rd));
							OF_EX_Latch.setB(immx);
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rs1)); //USE IN MA
						}

						//Arithmetic/Logical
						else if (opcode <= 21) {
							OF_EX_Latch.setBranchTarget(branchTarget); //OF NO USE
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
							OF_EX_Latch.setB(immx);
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd)); //OF NO USE
						}
					}
				}

				//R3 types - Arithmetic/Logical
				else {
					rs1 = (instruction << 5) >>> 27;
					rs2 = (instruction << 10) >>> 27;
					rd = (instruction << 15) >>> 27;

					OF_EX_Latch.setBranchTarget(currentPC + 1); //OF NO USE
					OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
					OF_EX_Latch.setB(this.containingProcessor.getRegisterFile().getValue(rs2));
					OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd)); //OF NO USE
				}


				OF_EX_Latch.setPc(currentPC);
				OF_EX_Latch.setInstruction(instruction);
			}

			OF_EX_Latch.setControlSignals(controlSignals);
			// System.out.println("CSIG NOT NULL");
			OF_EX_Latch.setEX_enable(true);
			IF_OF_Latch.setOF_enable(false);
		}
	}

}
