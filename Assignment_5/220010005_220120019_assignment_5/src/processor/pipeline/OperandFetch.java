package processor.pipeline;

import processor.Processor;
// import processor.pipeline.IF_EnableLatchType;
// import src.generic.Simulator;
// import processor.pipeline.IF_OF_LatchType;
import generic.Simulator;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	IF_EnableLatchType IF_EnableLatch;
	ControlUnit controlUnit;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.controlUnit = new ControlUnit();
	}
	
	public void performOF()
	{

		/*
		=========================================================================================
		Fetch the instruction from the IF_OF latch and create the corresponding control signals.
		=========================================================================================
		*/
		int instruction = IF_OF_Latch.getInstruction();
		ControlSignals controlSignals = controlUnit.createControlSignals(instruction);

		// int opcode = instruction >>> 27;
		int currentPC = IF_OF_Latch.getPc();

		int branchTarget;
		int immx;
		int rd;
		int rs2;
		int rs1;

		if(OF_EX_Latch.isEX_busy()) {
			//TODO SET NOPS (OF)??
			return;
		}

		if(containingProcessor.branchTakenCurrentCycle || instruction == 0)
		{
			OF_EX_Latch.setNop();
			return;
		}

		if(IF_OF_Latch.isOF_enable()) {
			 System.out.println("O");
			controlSignals.display();
			if(!controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {

				//RI and R2I types
				if (controlSignals.getControlSignal(ControlSignals.OperationSignals.IMMEDIATE.ordinal())) {
					//Unconditional Branch - RI (op2 unused)
					if (controlSignals.getControlSignal(ControlSignals.OperationSignals.JMP.ordinal())) {
						/*
						PC <- PC + rd +immx
						In ToyRISC, either rd or immx is zero for a jmp
						*/
						immx = (instruction << 10) >> 10;
						rd = (instruction << 5) >>> 27;
						int offsetFromRd = this.containingProcessor.getRegisterFile().getValue(rd);
						branchTarget = currentPC + offsetFromRd + immx;

						if (offsetFromRd != 0) {
							OF_EX_Latch.setBranchTarget(branchTarget);
							OF_EX_Latch.setB(offsetFromRd); //offsetFromRd
							OF_EX_Latch.setA(0); //OF NO USE
							OF_EX_Latch.setOp2(0); //OF NO USE
						}

						else {
							OF_EX_Latch.setBranchTarget(branchTarget);
							OF_EX_Latch.setB(immx);
							OF_EX_Latch.setA(0); //OF NO USE
							OF_EX_Latch.setOp2(0); //OF NO USE
						}
					}

					//Conditional Branches, Arithmetic/Logical, and Load/Store Operations - R2I
					else {
						rs1 = (instruction << 5) >>> 27;
						rd = (instruction << 10) >>> 27;
						immx = (instruction << 15) >> 15;
						branchTarget = currentPC + immx;

						//Conditional Branches
						if (
						controlSignals.getControlSignal(ControlSignals.OperationSignals.BEQ.ordinal()) ||
						controlSignals.getControlSignal(ControlSignals.OperationSignals.BNE.ordinal()) ||
						controlSignals.getControlSignal(ControlSignals.OperationSignals.BLT.ordinal()) ||
						controlSignals.getControlSignal(ControlSignals.OperationSignals.BGT.ordinal())
						) {
							// NOTE:
							// THE MULTIPLEXER DOES NOT FOLLOW THE DIAGRAM IN THE SLIDES EVEN THOUGH
							// CONDITIONAL BRANCHES ARE OF IMMEDIATE TYPE (DUE TO NO SEPERATE CMP INSTRUCTION)
							// THE VALUE OF B IS FROM RD AND NOT IMMX
							// ALU COMPARES A AND B

							if(
							containingProcessor.regLockVector[rd] > 0 ||
							containingProcessor.regLockVector[rs1] > 0 ||
							containingProcessor.regWrite[rd] > 0 ||
							containingProcessor.regWrite[rs1] > 0
							) {
								OF_EX_Latch.setNop();
								IF_EnableLatch.setIF_enable(false);
								Simulator.nStalls += 1;
								return;
							}
							else{
								IF_EnableLatch.setIF_enable(true);
							}
							OF_EX_Latch.setBranchTarget(branchTarget);
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
							OF_EX_Latch.setB(this.containingProcessor.getRegisterFile().getValue(rd));
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd));

						}

						else if (controlSignals.getControlSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
							// NOTE: (PART 1)
							// RS1 AND RD ARE SWITCHED FOR THE STORE INSTRUCTION, CAREFUL HERE, SIMPLERISC AND TOYRISC
							// ARE DIFFERENT
							// rd  <- rs1 + immx
							// Reg <- A   + B
							if(
							containingProcessor.regLockVector[rs1] > 0 ||
							containingProcessor.regWrite[rs1] > 0
							) {
								OF_EX_Latch.setNop();
								IF_EnableLatch.setIF_enable(false);
								Simulator.nStalls += 1;
								return;
							}
							else{
								IF_EnableLatch.setIF_enable(true);
								containingProcessor.regLockVector[rd] += 1;
							}

							OF_EX_Latch.setBranchTarget(branchTarget); //OF NO USE
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
							OF_EX_Latch.setB(immx);
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd)); //OF NO USE
						}

						else if (controlSignals.getControlSignal(ControlSignals.OperationSignals.STORE.ordinal())) {
							// NOTE: (PART 2)
							// RS1 AND RD ARE SWITCHED FOR THE STORE INSTRUCTION, CAREFUL HERE, SIMPLERISC AND TOYRISC
							// ARE DIFFERENT
							// rs1 -> rd + immx
							// Reg -> A  + B
							if(
							containingProcessor.regLockVector[rd] > 0 ||
							containingProcessor.regLockVector[rs1] > 0 ||
							containingProcessor.regWrite[rd] > 0 ||
							containingProcessor.regWrite[rs1] > 0
							) {
								OF_EX_Latch.setNop();
								IF_EnableLatch.setIF_enable(false);
								Simulator.nStalls += 1;
								return;
							}
							else{
								IF_EnableLatch.setIF_enable(true);
							}

							OF_EX_Latch.setBranchTarget(branchTarget); //OF NO USE
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rd));
							OF_EX_Latch.setB(immx);
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rs1)); //USE IN MA
						}

						//Arithmetic/Logical
						else if (controlSignals.getControlSignal(ControlSignals.OperationSignals.ALUSIGNAL.ordinal())) {
							if(
							containingProcessor.regLockVector[rs1] > 0 ||
							containingProcessor.regWrite[rs1] > 0
							) {
								OF_EX_Latch.setNop();
								IF_EnableLatch.setIF_enable(false);
								Simulator.nStalls += 1;
								return;
							}
							else{
								IF_EnableLatch.setIF_enable(true);
								containingProcessor.regLockVector[rd] += 1;
								if(controlSignals.getControlSignal(ControlSignals.OperationSignals.DIV.ordinal()))
									containingProcessor.regLockVector[31] += 1;
							}

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

					if(
					containingProcessor.regLockVector[rd] > 0 ||
					containingProcessor.regLockVector[rs1] > 0 ||
					containingProcessor.regWrite[rd] > 0 ||
					containingProcessor.regWrite[rs1] > 0
					) {
						OF_EX_Latch.setNop();
						IF_EnableLatch.setIF_enable(false);
						Simulator.nStalls += 1;
						return;
					}
					else{
						IF_EnableLatch.setIF_enable(true);
						containingProcessor.regLockVector[rd] += 1;
						if(controlSignals.getControlSignal(ControlSignals.OperationSignals.DIV.ordinal()))
							containingProcessor.regLockVector[31] += 1;
					}

					OF_EX_Latch.setBranchTarget(currentPC + 1); //OF NO USE
					OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
					OF_EX_Latch.setB(this.containingProcessor.getRegisterFile().getValue(rs2));
					OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd)); //OF NO USE
				}
				OF_EX_Latch.setPc(currentPC);
				OF_EX_Latch.setInstruction(instruction);
			}
			/*
			Emptying the latch when an end instruction passes through.
			*/
			// else {
			// 	OF_EX_Latch.setBranchTarget(0);
			// 	OF_EX_Latch.setA(0);
			// 	OF_EX_Latch.setB(0);
			// 	OF_EX_Latch.setOp2(0);
			// }
			OF_EX_Latch.setControlSignals(controlSignals);
			OF_EX_Latch.setEX_enable(true);
			// IF_OF_Latch.setOF_enable(false);
		}
	}

}
