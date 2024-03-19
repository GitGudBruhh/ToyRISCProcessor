package processor.pipeline;

import processor.Processor;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performIF()
	{
		boolean tmp = false;
		if(IF_EnableLatch.isIF_enable())
		{
			System.out.println("HALTTT");
			ControlSignals controlSignals = EX_IF_Latch.getControlSignals();
			RegisterFile regFileCopy = containingProcessor.getRegisterFile();
			int currentPC = regFileCopy.getProgramCounter();

			boolean isIgnoreBT = EX_IF_Latch.isIgnoreBT();
			int endInstr = 29 << 27;

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
			if(!containingProcessor.isStalled()) {
				if(controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {
					/*
					Emptying the latch when an end instruction passes through.
					Setting the processor to an idle state.
					*/
					// IF_OF_Latch.setInstruction(endInstr); //NOTE: USELESS
					IF_OF_Latch.setPc(currentPC - 4);
					// IF_OF_Latch.setIgnore(true); //NOTE: REDUNDANT, setIOE_clear() does this already
					containingProcessor.setIOE_clear();

					regFileCopy.setProgramCounter(currentPC - 2);
					containingProcessor.setRegisterFile(regFileCopy);

					IF_OF_Latch.setIgnore(false);
					//NOTE NOTE NOTE NOTE NOTE
						tmp = false;

					// IF_EnableLatch.setIF_enable(false);
					containingProcessor.setIdle(true);
					return;
				}
				else {
					/*
					Assume a branchPC exists.
					*/
					int branchPC = EX_IF_Latch.getBranchPC();
					int instruction = 200;

					/*
					=====================================================================================
					If the branch is taken, get the instruction from branchPC, push it into the latch.
					Set PC to branchPC + 1 for keeping IF ready for the next instruction.

					If branch is not taken, get the instruction from currentPC and push it to the latch.
					Set PC to currentPC + 1 for keeping IF ready for the next instruction.
					=====================================================================================
					*/
					if(controlSignals.getControlSignal(ControlSignals.OperationSignals.BRANCHTAKEN.ordinal()) && !isIgnoreBT) {
						instruction = containingProcessor.getMainMemory().getWord(branchPC);
						IF_OF_Latch.setInstruction(instruction);
						IF_OF_Latch.setPc(branchPC);

						regFileCopy.setProgramCounter(branchPC + 1);
						containingProcessor.setRegisterFile(regFileCopy);
						IF_OF_Latch.setIgnore(false);
						//NOTE NOTE NOTE NOTE NOTE
						tmp = false;

						// IF_OF_Latch.setOF_enable(true);
					}
					else {
						instruction = containingProcessor.getMainMemory().getWord(currentPC);
						IF_OF_Latch.setInstruction(instruction);
						IF_OF_Latch.setPc(currentPC);

						regFileCopy.setProgramCounter(currentPC + 1);
						containingProcessor.setRegisterFile(regFileCopy);
						IF_OF_Latch.setIgnore(false);
						//NOTE NOTE NOTE NOTE NOTE
						tmp = false;

						// IF_OF_Latch.setOF_enable(true);
					}

					System.out.println(instruction);
				}

				if(containingProcessor.isBranchTakenCurrentCycle()) {
					IF_OF_Latch.setIgnore(true);
				}

				System.out.println(currentPC);
			}
			MA_RW_Latch.setRW_enable(true);
			IF_EnableLatch.setIF_enable(false);
		}
		System.out.print("IF: ");
		System.out.println(tmp);
	}
}
