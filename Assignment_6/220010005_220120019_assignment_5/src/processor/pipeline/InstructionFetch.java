package processor.pipeline;

import processor.Clock;
import processor.Processor;

import configuration.Configuration;

import generic.Simulator;
import generic.Element;
import generic.Event;
import generic.EventQueue;
import generic.CacheReadEvent;
import generic.CacheResponseEvent;

public class InstructionFetch implements Element {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;

	int currentPCStored;
	boolean isBranchWhenBusy;
	int branchPCWhenBusy;
	
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
			if(!IF_OF_Latch.isOF_busy())
				IF_OF_Latch.setNop();
			return;
		}

		if(EX_IF_Latch.getControlSignals().getControlSignal(ControlSignals.OperationSignals.BRANCHTAKEN.ordinal())) {
			if(IF_EnableLatch.isIF_busy()) {
				this.isBranchWhenBusy = true;
				this.branchPCWhenBusy = EX_IF_Latch.getBranchPC();
				return;
			}
		}

		if(IF_EnableLatch.isIF_enable())
		{
			if(IF_EnableLatch.isIF_busy()) {
				if(!IF_OF_Latch.isOF_busy())
					IF_OF_Latch.setNop();
				return;
			}

			int programCounter = continueOrBranch();
			this.currentPCStored = programCounter;

			EventQueue eQueue = Simulator.getEventQueue();
			CacheReadEvent cReadEvent = new CacheReadEvent(Clock.getCurrentTime() + Configuration.L1i_latency,
															this,
															containingProcessor.getL1i_Cache(),
															programCounter);

			eQueue.addEvent(cReadEvent);
			System.out.println("Added IF event to queue");
			IF_EnableLatch.setIF_busy(true);
		}
	}


	private int continueOrBranch(){

		if(this.isBranchWhenBusy) {
			this.isBranchWhenBusy = false;
			return this.branchPCWhenBusy;
		}

		RegisterFile regFile = containingProcessor.getRegisterFile();
		int currentPC = regFile.getProgramCounter();

		ControlSignals controlSignals = EX_IF_Latch.getControlSignals();
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
			return branchPC;
		}
		else {
			return currentPC;
		}
	}

	@Override
	public void handleEvent(Event e) {
		System.out.println("Tried handling IF event");
		if(IF_OF_Latch.isOF_busy() || !IF_EnableLatch.isIF_enable()) {
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
			// IF_EnableLatch.setIF_busy_due_to_OF(true);
		}
		else
		{
			RegisterFile regFileCopy = containingProcessor.getRegisterFile();
			CacheResponseEvent event = (CacheResponseEvent) e;
			int instruction = event.getValue();

			IF_OF_Latch.setInstruction(instruction);
			IF_OF_Latch.setPc(currentPCStored);
			regFileCopy.setProgramCounter(currentPCStored + 1);
			containingProcessor.setRegisterFile(regFileCopy);

			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setIF_busy(false);
			IF_EnableLatch.setIF_enable(true);
			// IF_EnableLatch.setIF_busy_due_to_OF(false);

			System.out.println("Handled IF event!");

			if(isBranchWhenBusy) {
				IF_OF_Latch.setNop();
			}
		}
	}
}
