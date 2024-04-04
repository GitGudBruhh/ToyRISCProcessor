package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Statistics;
import processor.Clock;
import processor.Processor;

public class InstructionFetch implements Element{
	
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
			if(EX_IF_Latch.isIF_enable() && !IF_OF_Latch.isIF_branching_busy() &&!IF_OF_Latch.isIF_busy()){
				containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getPC());
				int currentPC = containingProcessor.getRegisterFile().getProgramCounter();

				Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime()+Configuration.mainMemoryLatency,
								this,
								containingProcessor.getMainMemory(),
								currentPC
								)
						);
				System.out.println("H1");
				IF_OF_Latch.setIF_branching_busy(true);


				EX_IF_Latch.setIF_enable(false);
				IF_OF_Latch.setOF_enable(false);
				System.out.println("IF: PC set to " + EX_IF_Latch.getPC());

			} // if EX_IF_Latch is enabled, set PC to EX_IF_Latch's PC and wait for next cycle (1 nop)
			else if(IF_EnableLatch.isIF_enable())
			{
				if(!IF_EnableLatch.isFreeze()){
					if(IF_OF_Latch.isIF_busy()){
						return;
					}
					int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
					
					Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
							Clock.getCurrentTime()+ Configuration.mainMemoryLatency,
							this,
							containingProcessor.getMainMemory(),
							currentPC
						)
					);
					System.out.println("H2");
					IF_OF_Latch.setIF_busy(true);
					IF_OF_Latch.setOF_enable(false);
				}else{
					IF_EnableLatch.setFreeze(false);
				}

			}
	}

	@Override
	public void handleEvent(Event e) {
		if(IF_OF_Latch.isOF_busy()|| IF_EnableLatch.isFreeze()){
			e.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(e);
			IF_EnableLatch.setFreeze(false);
			IF_OF_Latch.setOF_enable(true);
			return;
		}
		if(IF_OF_Latch.isIF_branching_busy()){
			IF_EnableLatch.setFreeze(false);
			IF_OF_Latch.setIF_branching_busy(false);
			containingProcessor.getOFUnit().Proceed = true;
			System.out.println("IF: Unfreezing");
			return;
		}
		MemoryResponseEvent event = (MemoryResponseEvent) e;
		Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions()+1);
		IF_OF_Latch.setInstruction(event.getValue());
		containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() + 1);
		IF_OF_Latch.setOF_enable(true);
		IF_OF_Latch.setIF_busy(false);
		IF_EnableLatch.setFreeze(false);
	}

}
