package processor.pipeline;

import processor.Processor;
import processor.Clock;

import configuration.Configuration;

import generic.Simulator;
import generic.Element;
import generic.Event;
import generic.EventQueue;
import generic.CacheReadEvent;
import generic.CacheResponseEvent;
import generic.CacheWriteEvent;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	int currentPCStored;
	int instructionStored;
	int mDataStored;
	int mAddrStored;
	long aluResultStored;
	ControlSignals cSigStored;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		ControlSignals controlSignals = EX_MA_Latch.getControlSignals();
		int instruction = EX_MA_Latch.getInstruction();
		int currentPC = EX_MA_Latch.getPc();
		long aluResult = EX_MA_Latch.getAluResult();
		int op2 = EX_MA_Latch.getOp2();

		if(instruction == 0)
		{
			MA_RW_Latch.setNop();
			return;
		}

		if(EX_MA_Latch.isMA_enable()) {

			if(EX_MA_Latch.isMA_busy()) {
				return;
			}

			if(!controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {

				int memoryAddress = (int) (aluResult & 0x00000000ffffffffL); //memoryAddressRegister
				int memoryData = op2; //memoryDataRegister
				// int ldResult = 0;

				if(controlSignals.getControlSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
					// ldResult = containingProcessor.getMainMemory().getWord(memoryAddress);

					EventQueue eQueue = Simulator.getEventQueue();
					CacheReadEvent cReadEvent = new CacheReadEvent(Clock.getCurrentTime() + Configuration.L1d_latency,
																	this,
																	containingProcessor.getL1d_Cache(),
																	memoryAddress);

					eQueue.addEvent(cReadEvent);
					System.out.println("Added CacheRead event to queue (MA)");

					this.aluResultStored = aluResult;
					this.mAddrStored = memoryAddress;
					this.currentPCStored = currentPC;
					this.instructionStored = instruction;
					this.cSigStored = controlSignals;

					EX_MA_Latch.setMA_busy(true);
				}

				else if(controlSignals.getControlSignal(ControlSignals.OperationSignals.STORE.ordinal())) {
					// MainMemory newMemory = containingProcessor.getMainMemory();
					// newMemory.setWord(memoryAddress, memoryData);
					// containingProcessor.setMainMemory(newMemory);

					EventQueue eQueue = Simulator.getEventQueue();
					CacheWriteEvent cWriteEvent = new CacheWriteEvent(Clock.getCurrentTime() + Configuration.L1d_latency,
																	this,
																	containingProcessor.getL1d_Cache(),
																	memoryAddress,
																	memoryData);

					eQueue.addEvent(cWriteEvent);
					System.out.println("Added CacheWrite event to queue (MA)");

					this.aluResultStored = aluResult;
					this.mAddrStored = memoryAddress;
					this.mDataStored = memoryData;
					this.currentPCStored = currentPC;
					this.instructionStored = instruction;
					this.cSigStored = controlSignals;
					// controlSignals.display();

					EX_MA_Latch.setMA_busy(true);

				}

				else {
					MA_RW_Latch.setInstruction(instruction);
					MA_RW_Latch.setPc(currentPC);
					MA_RW_Latch.setLdResult(0);
					MA_RW_Latch.setAluResult(aluResult);
					MA_RW_Latch.setControlSignals(controlSignals);
					System.out.println("WRITING THIS TO MA_RW:");
					controlSignals.display();
				}
			}

			/*
			Emptying the latch when an END
			instruction passes through
			*/
			else {
				MA_RW_Latch.setInstruction(instruction);
				MA_RW_Latch.setPc(currentPC);
				MA_RW_Latch.setLdResult(0);
				MA_RW_Latch.setAluResult(0);
				MA_RW_Latch.setControlSignals(controlSignals);
			}

			// MA_RW_Latch.setControlSignals(controlSignals);
			MA_RW_Latch.setRW_enable(true);
		}
	}

	@Override
	public void handleEvent(Event e) {
		CacheResponseEvent event = (CacheResponseEvent) e;

		int ldResult = event.getValue();

		if(cSigStored.getControlSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
			// System.out.println(event.getRequestType());

			MA_RW_Latch.setInstruction(instructionStored);
			MA_RW_Latch.setPc(currentPCStored);
			MA_RW_Latch.setLdResult(ldResult);
			MA_RW_Latch.setAluResult(aluResultStored);
			MA_RW_Latch.setControlSignals(cSigStored);
			EX_MA_Latch.setNop();
			System.out.println("Handled MA read event!");

			System.out.println("WRITING THIS TO MA_RW:");
			System.out.println("----");
			cSigStored.display();
			System.out.println("LDRES: "+ldResult);
			System.out.println("----");

			EX_MA_Latch.setNop();
		}

		if(cSigStored.getControlSignal(ControlSignals.OperationSignals.STORE.ordinal())) {
			// System.out.println(event.getRequestType());

			MA_RW_Latch.setInstruction(instructionStored);
			MA_RW_Latch.setPc(currentPCStored);
			MA_RW_Latch.setLdResult(0);
			MA_RW_Latch.setAluResult(aluResultStored);
			MA_RW_Latch.setControlSignals(cSigStored);
			EX_MA_Latch.setNop();
			System.out.println("Handled MA write event!");

			System.out.println("WRITING THIS TO MA_RW:");
			System.out.println("----");
			cSigStored.display();
			System.out.println("LDRES: 0");
			System.out.println("----");

			EX_MA_Latch.setNop();

		}

		MA_RW_Latch.setRW_enable(true);
		EX_MA_Latch.setMA_busy(false);
	}

}
