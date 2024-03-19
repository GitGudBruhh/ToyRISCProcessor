package processor.pipeline;

import processor.Processor;
import processor.memorysystem.MainMemory;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performMA()
	{
		//TODO
		boolean isIgnore = EX_MA_Latch.isIgnore();
		ControlSignals controlSignals = EX_MA_Latch.getControlSignals();

		if(EX_MA_Latch.isMA_enable() && !isIgnore) {
				if(!controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {
					int currentPC = EX_MA_Latch.getPc();
					long aluResult = EX_MA_Latch.getAluResult();
					int op2 = EX_MA_Latch.getOp2();
					int instruction = EX_MA_Latch.getInstruction();


					int memoryAddress = (int) (aluResult & 0x00000000ffffffffL); //memoryAddressRegister
					int memoryData = op2; //memoryDataRegister
					int ldResult = 0;

					if(controlSignals.getControlSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
						ldResult = containingProcessor.getMainMemory().getWord(memoryAddress);
					}

					if(controlSignals.getControlSignal(ControlSignals.OperationSignals.STORE.ordinal())) {
						MainMemory newMemory = containingProcessor.getMainMemory();
						newMemory.setWord(memoryAddress, memoryData);
						containingProcessor.setMainMemory(newMemory);

					}

					MA_RW_Latch.setPc(currentPC);
					MA_RW_Latch.setLdResult(ldResult);
					MA_RW_Latch.setAluResult(aluResult);
					MA_RW_Latch.setInstruction(instruction);
				}
		}
		MA_RW_Latch.setIgnore(isIgnore);
		MA_RW_Latch.setControlSignals(controlSignals);
		EX_MA_Latch.setMA_enable(false);
		// MA_RW_Latch.setRW_enable(true);
		OF_EX_Latch.setEX_enable(true);

		System.out.print("MA: ");
		System.out.println(isIgnore);
	}
}
