package processor.pipeline;

import processor.Processor;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(containingProcessor.branchTakenCurrentCycle)
		{
			IF_OF_Latch.setNop();
			return;
		}

		if(IF_EnableLatch.isIF_enable())
		{
			System.out.println("I");
			ControlSignals controlSignals = EX_IF_Latch.getControlSignals();
			RegisterFile regFileCopy = containingProcessor.getRegisterFile();
			int currentPC = regFileCopy.getProgramCounter();

			System.out.println(currentPC);

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
			// if(controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {
			// 	/*
			// 	Emptying the latch when an end instruction passes through.
			// 	Setting the processor to an idle state.
			// 	*/
			// 	IF_OF_Latch.setInstruction(0);
			// 	IF_OF_Latch.setPc(0);
   //
			// 	IF_EnableLatch.setIF_enable(false);
			// 	containingProcessor.setIdle(true);
			// 	return;
			// }
			/*else*/ {
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
				if(controlSignals.getControlSignal(ControlSignals.OperationSignals.BRANCHTAKEN.ordinal())) {
					int instruction = containingProcessor.getMainMemory().getWord(branchPC);
					IF_OF_Latch.setInstruction(instruction);
					IF_OF_Latch.setPc(branchPC);

					regFileCopy.setProgramCounter(branchPC + 1);
					containingProcessor.setRegisterFile(regFileCopy);

					IF_OF_Latch.setOF_enable(true);
					// IF_EnableLatch.setIF_enable(false);
				}
				else {
					int instruction = containingProcessor.getMainMemory().getWord(currentPC);
					IF_OF_Latch.setInstruction(instruction);
					IF_OF_Latch.setPc(currentPC);

					regFileCopy.setProgramCounter(currentPC + 1);
					containingProcessor.setRegisterFile(regFileCopy);

					IF_OF_Latch.setOF_enable(true);
					// IF_EnableLatch.setIF_enable(false);
				}
			}
		}
	}

}
