package processor.pipeline;

import processor.Processor;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;
	Interlocks interlocks;
	
	public InstructionFetch(Processor containingProcessor,
	IF_EnableLatchType iF_EnableLatch,
	IF_OF_LatchType iF_OF_Latch,
	EX_IF_LatchType eX_IF_Latch,
	MA_RW_LatchType mA_RW_Latch,
	Interlocks interlocks) {
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.MA_RW_Latch = mA_RW_Latch; // USED ONLY FOR TURNING ON THE STAGE
		this.interlocks = interlocks;
	}
	
	public void performIF() {
		ControlSignals controlSignals = EX_IF_Latch.getControlSignals();
		RegisterFile regFileCopy = containingProcessor.getRegisterFile();
		int currentPC = regFileCopy.getProgramCounter();

		if(isStallRequired()) {
			IF_EnableLatch.setIF_enable(false);
			MA_RW_Latch.setRW_enable(true);
			return;
		}

		if(IF_EnableLatch.isIF_enable())
		{
			/*
			===================================================================================================
			An idle processor denotes the start of a program, and an end instruction sets the processor to idle.
			A non idle processor continues with the instruction fetch.
			===================================================================================================
			*/
			if(containingProcessor.isIdle())
				containingProcessor.setIdle(false);

			/*
			===========================================================================================
			If the previous instruction was an end instruction, just set the processor to an idle state.
			The simulator takes care of the processor idling by setting simulationComplete to true.
			===========================================================================================
			*/
			if(controlSignals.getOperationSignal(ControlSignals.OperationSignals.END.ordinal())) {
				/*
				Emptying the latch when an end instruction passes through.
				Setting the processor to an idle state.
				*/
				IF_OF_Latch.setInstruction(0);
				IF_OF_Latch.setPc(0);

				IF_EnableLatch.setIF_enable(false);
				MA_RW_Latch.setRW_enable(true);
				containingProcessor.setIdle(true);
				return;
			}
			else {
				/*
				Assume a branchPC exists.
				*/
				int branchPC = EX_IF_Latch.getBranchPC();

				/*
				=====================================================================================
				If the branch is taken, get the instruction from branchPC, push it into the latch.
				Set PC to branchPC + 1 for keeping IF ready for the next instruction.

				If branch is not taken, get the instruction from currentPC and push it to the latch.
				Set PC to currentPC + 1 for keeping IF ready for the next instruction.
				=====================================================================================
				*/
				if(controlSignals.getMiscSignal(ControlSignals.MiscSignals.BRANCHTAKEN.ordinal())) {
					int instruction = containingProcessor.getMainMemory().getWord(branchPC);
					IF_OF_Latch.setInstruction(instruction);
					IF_OF_Latch.setPc(branchPC);

					regFileCopy.setProgramCounter(branchPC + 1);
					containingProcessor.setRegisterFile(regFileCopy);

					IF_EnableLatch.setIF_enable(false);
					// IF_OF_Latch.setOF_enable(true);
					MA_RW_Latch.setRW_enable(true);
				}
				else {
					int instruction = containingProcessor.getMainMemory().getWord(currentPC);
					IF_OF_Latch.setInstruction(instruction);
					IF_OF_Latch.setPc(currentPC);

					regFileCopy.setProgramCounter(currentPC + 1);
					containingProcessor.setRegisterFile(regFileCopy);

					IF_EnableLatch.setIF_enable(false);
					// IF_OF_Latch.setOF_enable(true);
					MA_RW_Latch.setRW_enable(true);
				}
			}
		}
	}

	/*
	NOTE:
	The IF stage has to check for stalls in the current cycle before write
	(stage stalls buffer), as well as before pulling the instruction from PC
	(stage stalls).
	IF, OF stage halt if either stall/stall buffer has 1s.

	a) Proceed as normal
		Stall		: 0 0 0 0 0
		Stall Buffer: 0 0 0 0 0
		Producer	: 1 0 0 0 0
		isRegLocked	: false (buffer), false

	b) Proceed as normal
		Stall		: 0 0 0 0 0
		Stall Buffer: 0 0 0 0 0
		Producer	: 0 1 0 0 0
		isRegLocked	: true (buffer), false //true written in current cycle

	In between cycles, regLock <- true

	======================================
	GAP_0

	c) Halt, OF encountered data interlock
		Stall		: 0 0 0 0 0
		Stall Buffer: 1 1 0 0 0
		Producer	: 0 0 1 0 0
		isRegLocked	: true, true

	d) Halt, OF still under data interlock
		Stall		: 1 1 0 0 0
		Stall Buffer: 1 1 0 0 0
		Producer	: 0 0 0 1 0
		isRegLocked	: true (buffer), true

	e) Halt, RW occured in current cycle
		Stall		: 1 1 0 0 0
		Stall Buffer: 0 0 0 0 0	//Set stageStallBuffer to false by reading dI_buf
		Producer	: 0 0 0 0 1
		isRegLocked	: false (buffer), true

	f) Proceed as normal
		Stall		: 0 0 0 0 0
		Stall Buffer: 0 0 0 0 0
		Producer	: 0 0 0 0 0 ->
		isRegLocked	: false (buffer), false
	========================================
	GAP_1

	c) Proceed as normal
		Stall		: 0 0 0 0 0
		Stall Buffer: 0 0 0 0 0
		Producer	: 0 0 1 0 0
		isRegLocked	: true, true

	d) Halt, OF encountered data interlock
		Stall		: 0 0 0 0 0
		Stall Buffer: 1 1 0 0 0
		Producer	: 0 0 0 1 0
		isRegLocked	: true (buffer), true

	e) Halt, RW occured in current cycle
		Stall		: 1 1 0 0 0
		Stall Buffer: 0 0 0 0 0	//Set stageStallBuffer to false by reading dI_buf
		Producer	: 0 0 0 0 1
		isRegLocked	: false (buffer), true

	f) Proceed as normal
		Stall		: 0 0 0 0 0
		Stall Buffer: 0 0 0 0 0
		Producer	: 0 0 0 0 0 ->
		isRegLocked	: false (buffer), false
	========================================
	GAP_2

	c) Proceed as normal
		Stall		: 0 0 0 0 0
		Stall Buffer: 0 0 0 0 0
		Producer	: 0 0 1 0 0
		isRegLocked	: true, true

	d) Proceed as normal
		Stall		: 0 0 0 0 0
		Stall Buffer: 0 0 0 0 0
		Producer	: 0 0 0 1 0
		isRegLocked	: true (buffer), true

	e) Halt, OF encountered data interlock, RW occured in current cycle
		Stall		: 1 1 0 0 0
		Stall Buffer: 0 0 0 0 0	//Set stageStallBuffer to false by reading dI_buf
		Producer	: 0 0 0 0 1
		isRegLocked	: false (buffer), true

		ERRORED_S	: 0 0 0 0 0
		ERRORED_SB	: 0 0 0 0 0 //NOTE: THIS WAS PRODUCED BEFORE THE FIX, LEADING TO
								//NO HALTS CHECK SOLUTION FOR DETAILS OF IMPLEMENTATION

	f) Proceed as normal
		Stall		: 0 0 0 0 0
		Stall Buffer: 0 0 0 0 0
		Producer	: 0 0 0 0 0 ->
		isRegLocked	: false (buffer), false

	pipelineStallBuffer[] writes to pipelineStalls[] at the end of each cycle

	Simple solution:
	isRegLocked: true (buffer), true -> Stall Buffer = 1, Stall = prevSB //GAP_2, GAP_1 starts from here
	isRegLocked: false (buffer), true -> Stall Buffer = 0, Stall = 1	//GAP_0 starts from here
	isRegLocked: true (buffer), false -> Stall Buffer = 1, Stall = 0
	isRegLocked: false (buffer), false -> Stall Buffer = 0, Stall = 0 //automatically taken care of
	*/

	private boolean isStallRequired(Interlocks interlocks) {
		if(interlocks.getStageStallBuf(Interlocks.Stages.IF.ordinal()) ||
		interlocks.getStageStall(Interlocks.Stages.IF.ordinal()))
			return true;
		else
			return false;
	}
}
