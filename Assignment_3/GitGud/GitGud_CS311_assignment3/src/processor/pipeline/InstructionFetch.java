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
		if(IF_EnableLatch.isIF_enable())
		{
			// System.out.println("==============================================================================");
   //
			// ControlSignals lastControlSignals = EX_IF_Latch.getControlSignals();
			// if(containingProcessor.isIdle())
			// 	containingProcessor.setIdle(false);
   //
			// System.out.println("BEFORE IF");
			// lastControlSignals.display();
   //
			// RegisterFile regFileCopy = containingProcessor.getRegisterFile();
			// int currentPC = regFileCopy.getProgramCounter();
   //
			// // System.out.println(currentPC);
   //
			// if(lastControlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {
			// 	containingProcessor.setIdle(true);
			// 	IF_EnableLatch.setIF_enable(false);
   //
			// 	System.out.println("BRANCHPC AAAA: ");
			// }
			// else {
			// 	if(lastControlSignals.getControlSignal(ControlSignals.OperationSignals.BRANCHTAKEN.ordinal())) {
			// 		int branchPC = EX_IF_Latch.getBranchPC();
			// 		currentPC = branchPC;
			// 		IF_OF_Latch.setPc(branchPC);
			// 		regFileCopy.setProgramCounter(branchPC+1);
			// 	}
			// 	else {
			// 		IF_OF_Latch.setPc(currentPC);
			// 		regFileCopy.setProgramCounter(currentPC + 1);
			// 	}
   //
			// 	containingProcessor.setRegisterFile(regFileCopy);
			// 	int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			// 	IF_OF_Latch.setInstruction(newInstruction);
			// 	IF_OF_Latch.setOF_enable(true);
			// 	System.out.println("BAHRBWBQQUUR");
			// 	IF_EnableLatch.setIF_enable(false);
			// }

			ControlSignals controlSignals = EX_IF_Latch.getControlSignals();
			System.out.println("BEFORE IF");
			controlSignals.display();

			RegisterFile regFileCopy = containingProcessor.getRegisterFile();
			int currentPC = regFileCopy.getProgramCounter();

			if(containingProcessor.isIdle())
				containingProcessor.setIdle(false);

			if(controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {
				int instruction = containingProcessor.getMainMemory().getWord(currentPC);
				IF_OF_Latch.setInstruction(instruction);
				IF_OF_Latch.setPc(currentPC);

				regFileCopy.setProgramCounter(currentPC);
				containingProcessor.setRegisterFile(regFileCopy);

				IF_OF_Latch.setOF_enable(true);
				IF_EnableLatch.setIF_enable(false);

				containingProcessor.setIdle(true);
				return;
			}
			else {
				int branchPC = EX_IF_Latch.getBranchPC();
				System.out.println("BRNACH PC QFWFQWF: " + branchPC);

				if(controlSignals.getControlSignal(ControlSignals.OperationSignals.BRANCHTAKEN.ordinal())) {
					int instruction = containingProcessor.getMainMemory().getWord(branchPC);
					IF_OF_Latch.setInstruction(instruction);
					IF_OF_Latch.setPc(branchPC);

					regFileCopy.setProgramCounter(branchPC + 1);
					containingProcessor.setRegisterFile(regFileCopy);

					IF_OF_Latch.setOF_enable(true);
					IF_EnableLatch.setIF_enable(false);
				}
				else {
					int instruction = containingProcessor.getMainMemory().getWord(currentPC);
					IF_OF_Latch.setInstruction(instruction);
					IF_OF_Latch.setPc(currentPC);

					regFileCopy.setProgramCounter(currentPC + 1);
					containingProcessor.setRegisterFile(regFileCopy);

					IF_OF_Latch.setOF_enable(true);
					IF_EnableLatch.setIF_enable(false);
				}
			}

		}
	}

}
