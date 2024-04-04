package processor.pipeline;

import generic.Simulator;
import generic.Instruction;
import generic.Instruction.OperationType;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	EX_MA_LatchType EX_MA_Latch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch, EX_MA_LatchType eX_MA_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.EX_MA_Latch = eX_MA_Latch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			Instruction instruction = MA_RW_Latch.getInstruction();
			OperationType op_type = instruction.getOperationType();
			int alu_result = MA_RW_Latch.getALU_result();
			boolean proceed = true;
			int opcode = MA_RW_Latch.getopcode();
			// MA_RW_Latch.setopcode(opcode);
			if (opcode==22)
			{
				EX_MA_Latch.setMA_busy(false);
				int load_result = MA_RW_Latch.getLoad_result();
				int rd = instruction.getDestinationOperand().getValue();
				containingProcessor.getRegisterFile().setValue(rd, load_result);
			}
			else if (opcode==29)
			{
				Simulator.setSimulationComplete(true);
				proceed = false;
			}
			else
			{
				if (opcode!=23 && opcode!= 24 && opcode!= 25 && opcode!=26 && opcode!=27 && opcode!=28)
				{
					int rd = instruction.getDestinationOperand().getValue();
					rd = instruction.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(rd, alu_result);
				}
			}
			IF_EnableLatch.setIF_enable(proceed);
		}else{
			try{
				if(MA_RW_Latch.getInstruction().getOperationType() == OperationType.end){
					IF_EnableLatch.setIF_enable(false);
				}
				else{
					IF_EnableLatch.setIF_enable(true);
				}
			} catch(Exception e){
				IF_EnableLatch.setIF_enable(true);
			}
		}
	}

}
