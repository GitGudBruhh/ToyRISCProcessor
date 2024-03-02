package processor.pipeline;

import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		int currentPC = EX_MA_Latch.getPc();
		long aluResult = EX_MA_Latch.getAluResult();
		int op2 = EX_MA_Latch.getOp2();
		int instruction = EX_MA_Latch.getInstruction();
		ControlSignals controlSignals = EX_MA_Latch.getControlSignals();
	}

}
