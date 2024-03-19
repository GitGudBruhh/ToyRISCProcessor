package processor.pipeline;

import processor.Processor;
// import src.generic.Simulator;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	IF_EnableLatchType IF_EnableLatch;
	ControlUnit controlUnit;
	Interlocks interlocks;
	
	public OperandFetch(Processor containingProcessor,
	IF_OF_LatchType iF_OF_Latch,
	OF_EX_LatchType oF_EX_Latch,
	IF_EnableLatchType iF_Enable_Latch,
	Interlocks interlocks) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.IF_EnableLatch = iF_Enable_Latch; // USED ONLY FOR TURNING ON THE STAGE
		this.controlUnit = new ControlUnit();
		this.interlocks = interlocks;

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

		boolean r_lock_buf = false;
		boolean r_lock = false;

		/*
		===================================================================================================
		Nop if control signals have an IGNORE signal
		===================================================================================================
		*/
		// if(controlSignals.getMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal())) {
		// 	IF_OF_Latch.setOF_enable(false);
		// 	return;
		// }

		if(isStallRequired()) {
			IF_OF_Latch.setOF_enable(false);
			IF_EnableLatch.setIF_enable(true);
			return;
		}

		DataInterlock dataInterlock = interlocks.readDataInterlock();
		DataInterlock dI_buf = interlocks.getDataInterlockBuf();

		if(IF_OF_Latch.isOF_enable()) {

			if(!controlSignals.getOperationSignal(ControlSignals.OperationSignals.END.ordinal())) {

				int opcode = instruction >>> 27;
				int currentPC = IF_OF_Latch.getPc();

				int branchTarget;
				int immx;
				int rd;
				int rs2;
				int rs1;

				/*
				=================
				RI and R2I types
				=================
				*/
				if (controlSignals.getMiscSignal(ControlSignals.MiscSignals.IMMEDIATE.ordinal())) {
					/*
					==================================================
					Unconditional Branch - RI (op2 unused)
					==================================================
					*/
					if (controlSignals.getOperationSignal(ControlSignals.OperationSignals.JMP.ordinal())) {
						/*
						PC <- PC + rd + immx
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

					/*
					===========================================================================
					Conditional Branches, Arithmetic/Logical, and Load/Store Operations - R2I
					===========================================================================
					*/
					else {
						rs1 = (instruction << 5) >>> 27;
						rd = (instruction << 10) >>> 27;
						immx = (instruction << 15) >> 15;
						branchTarget = currentPC + immx;

						//Conditional Branches - R2I
						if (
						controlSignals.getOperationSignal(ControlSignals.OperationSignals.BEQ.ordinal()) ||
						controlSignals.getOperationSignal(ControlSignals.OperationSignals.BNE.ordinal()) ||
						controlSignals.getOperationSignal(ControlSignals.OperationSignals.BLT.ordinal()) ||
						controlSignals.getOperationSignal(ControlSignals.OperationSignals.BGT.ordinal())
						) {
							// NOTE:
							// THE MULTIPLEXER DOES NOT FOLLOW THE DIAGRAM IN THE SLIDES EVEN THOUGH
							// CONDITIONAL BRANCHES ARE OF IMMEDIATE TYPE (DUE TO NO SEPERATE CMP INSTRUCTION)
							// THE VALUE OF B IS FROM RD AND NOT IMMX
							// ALU COMPARES A AND B

							if(dataInterlock.isRegisterLocked(rs1) || dataInterlock.isRegisterLocked(rd)) {
								controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), true);
								r_lock = true;
							}
							else {
								controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), false);
							}

							if(dI_buf.isRegisterLocked(rs1) || dI_buf.isRegisterLocked(rd)) {
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), true);
								r_lock_buf = true;
							}
							else {
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), false);
							}

							OF_EX_Latch.setBranchTarget(branchTarget);
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
							OF_EX_Latch.setB(this.containingProcessor.getRegisterFile().getValue(rd));
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd));
							OF_EX_Latch.setControlSignals(controlSignals);

						}

						else if (controlSignals.getOperationSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
							// NOTE: (PART 1)
							// RS1 AND RD ARE SWITCHED FOR THE STORE INSTRUCTION, CAREFUL HERE, SIMPLERISC AND TOYRISC
							// ARE DIFFERENT
							// rd  <- rs1 + immx
							// Reg <- A   + B

							if(dataInterlock.isRegisterLocked(rs1)) {
								controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), true);
								r_lock = true;
							}
							else {
								controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), false);
							}

							if(dI_buf.isRegisterLocked(rs1)) {
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), true);
								r_lock_buf = true;
							}
							else {
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), false);
							}

							OF_EX_Latch.setBranchTarget(branchTarget); //OF NO USE
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
							OF_EX_Latch.setB(immx);
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd)); //OF NO USE
							OF_EX_Latch.setControlSignals(controlSignals);
						}

						else if (controlSignals.getOperationSignal(ControlSignals.OperationSignals.STORE.ordinal())) {
							// NOTE: (PART 2)
							// RS1 AND RD ARE SWITCHED FOR THE STORE INSTRUCTION, CAREFUL HERE, SIMPLERISC AND TOYRISC
							// ARE DIFFERENT
							// rs1 -> rd + immx
							// Reg -> A  + B
							if(dataInterlock.isRegisterLocked(rs1) || dataInterlock.isRegisterLocked(rd)) {
								controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), true);
								r_lock = true;
							else {
								controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), false);
							}

							if(dI_buf.isRegisterLocked(rs1) || dI_buf.isRegisterLocked(rd)) {
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), true);
								r_lock_buf = true;
							}
							else {
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), false);
							}

							OF_EX_Latch.setBranchTarget(branchTarget); //OF NO USE
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rd));
							OF_EX_Latch.setB(immx);
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rs1)); //USE IN MA
							OF_EX_Latch.setControlSignals(controlSignals);
						}


						//Arithmetic/Logical - R2I
						else if (controlSignals.getMiscSignal(ControlSignals.MiscSignals.ALUSIGNAL.ordinal())) {

							if(dataInterlock.isRegisterLocked(rs1)) {
								controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), true);
								r_lock = true;
							}
							else {
								controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), false);
							}

							if(dI_buf.isRegisterLocked(rs1)) {
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), true);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), true);
								r_lock_buf = true;
							}
							else {
								interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), false);
								interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), false);
							}

							OF_EX_Latch.setBranchTarget(branchTarget); //OF NO USE
							OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
							OF_EX_Latch.setB(immx);
							OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd)); //OF NO USE
							OF_EX_Latch.setControlSignals(controlSignals);
						}
					}
				}

				/*
				=============================
				R3 types - Arithmetic/Logical
				=============================
				*/
				else {
					rs1 = (instruction << 5) >>> 27;
					rs2 = (instruction << 10) >>> 27;
					rd = (instruction << 15) >>> 27;

					if(dataInterlock.isRegisterLocked(rs1) || dataInterlock.isRegisterLocked(rs2)) {
						controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), true);
						interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), true);
						interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), true);
						r_lock = true;
					}
					else {
						controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), false);
						interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), false);
						interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), false);
					}

					if(dI_buf.isRegisterLocked(rs1) || dI_buf.isRegisterLocked(rs2)) {
						interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), true);
						interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), true);
						r_lock_buf = true;
					}
					else {
						interlocks.setStageStallBuf(Interlocks.Stages.OF.ordinal(), false);
						interlocks.setStageStallBuf(Interlocks.Stages.IF.ordinal(), false);
					}

					OF_EX_Latch.setBranchTarget(currentPC + 1); //OF NO USE
					OF_EX_Latch.setA(this.containingProcessor.getRegisterFile().getValue(rs1));
					OF_EX_Latch.setB(this.containingProcessor.getRegisterFile().getValue(rs2));
					OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd)); //OF NO USE
					OF_EX_Latch.setControlSignals(controlSignals);
				}
				OF_EX_Latch.setPc(currentPC);
				OF_EX_Latch.setInstruction(instruction);

				//Added to ensure GAP_2, stall type (e) is taken care of
				if(r_lock && !r_lock_buf) {
					interlocks.setStageStall(Interlocks.Stages.OF.ordinal(), true);
					interlocks.setStageStall(Interlocks.Stages.IF.ordinal(), true);
				}

				//====================================================================================================

				/*
				===========================================================================
				Write to interlock buffer if it is a register writeback instruction
				and OF stage is not stalled (in stage stall and stage stall buffer)
				===========================================================================
				*/
				if(!getStageStallBuf(Interlocks.Stages.OF.ordinal()) && !getStageStall(Interlocks.Stages.OF.ordinal())) {
					if(controlSignals.getMiscSignal(ControlSignals.MiscSignals.WB.ordinal()))) {
						dI_buf.setRegisterLock(rd, true);
						interlocks.setDataInterlockBuf(dI_buf);
					}
				}
			}

			/*
			============================================================================
			Emptying the latch when an end instruction passes through.
			============================================================================
			*/
			else {
				OF_EX_Latch.setBranchTarget(0);
				OF_EX_Latch.setA(0);
				OF_EX_Latch.setB(0);
				OF_EX_Latch.setOp2(0);
			}

			IF_OF_Latch.setOF_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

	private boolean isStallRequired() {
		if(interlocks.getStageStall(Interlocks.Stages.OF.ordinal()))
			return true;
		else
			return false;
	}
}
