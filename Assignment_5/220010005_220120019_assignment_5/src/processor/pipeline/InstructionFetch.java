package processor.pipeline;

import generic.Simulator;
import processor.Clock;
import processor.Processor;
import configuration.Configuration;
import generic.EventQueue;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Element;
import generic.Event;

public class InstructionFetch implements Element {
	
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

		if(containingProcessor.branchTakenCurrentCycle) {
			IF_OF_Latch.setNop();
			return;
		}

		if(IF_EnableLatch.isIF_enable())
		{

			if(IF_EnableLatch.isIF_busy()) {
				return;
			}

			EventQueue e_queue = Simulator.getEventQueue();
			MemoryReadEvent mr_event = new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,
															this,
															containingProcessor.getMainMemory(),
															containingProcessor.getRegisterFile().getProgramCounter());

			this.fetchFromPc();
		}
	}


	private void fetchFromPc() {
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

	@Override
	public void handleEvent(Event e) {
		if(IF_OF_Latch.isOF_busy()) {
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else
		{
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			this.fetchFromPc();

			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setIF_busy(false);
		}
	}
}
