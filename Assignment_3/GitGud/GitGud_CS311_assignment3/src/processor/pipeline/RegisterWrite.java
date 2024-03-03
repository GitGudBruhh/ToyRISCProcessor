package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		//TODO
		ControlSignals controlSignals = MA_RW_Latch.getControlSignals();
		if(MA_RW_Latch.isRW_enable())
		{
			if(!controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {
				int pc = MA_RW_Latch.getPc();
				int ldResult = MA_RW_Latch.getLdResult();
				long aluResult = MA_RW_Latch.getAluResult();
				int instruction = MA_RW_Latch.getInstruction();

				boolean isAluResOverflow = true;
				RegisterFile newRegFile = containingProcessor.getRegisterFile();

				if((aluResult >>> 32) == 0 || (aluResult >>> 32) == 0x00000000ffffffffL)
					isAluResOverflow = false;
				// int opcode = instruction >>> 27;

				if(controlSignals.getControlSignal(ControlSignals.OperationSignals.WB.ordinal())) {
					if(controlSignals.getControlSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
						int rd = (instruction << 10) >>> 27;
						newRegFile.setValue(rd, ldResult);

						// System.out.println("LOAD RESULT IN RD IS");
						// System.out.println(newRegFile.getValue(rd));
						// System.out.println(rd);
						// containingProcessor.setRegisterFile(newRegFile);
						// System.out.println("DID IT REALLY SET THE REGISTER FILE");
						// RegisterFile bruh = containingProcessor.getRegisterFile();
						// System.out.println(bruh.getValue(rd));
					}

					else if (controlSignals.getControlSignal(ControlSignals.OperationSignals.IMMEDIATE.ordinal())) {
						int rd = (instruction << 15) >>> 27;
						newRegFile.setValue(rd, (int) aluResult);
						if(isAluResOverflow)
							newRegFile.setValue(31, (int) (aluResult >>> 32));
						containingProcessor.setRegisterFile(newRegFile);
					}

					else {
						int rd = (instruction << 10) >>> 27;
						newRegFile.setValue(rd, (int) aluResult);
						if(isAluResOverflow)
							newRegFile.setValue(31, (int) (aluResult >>> 32));
						containingProcessor.setRegisterFile(newRegFile);
					}
				}
			}
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
