package processor.pipeline;

import generic.Simulator;
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
		//TODO
		ControlSignals controlSignals = MA_RW_Latch.getControlSignals();
		boolean isIgnore = MA_RW_Latch.isIgnore();

		if(MA_RW_Latch.isRW_enable() && !isIgnore) {
			if(!controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {
				int pc = MA_RW_Latch.getPc();
				int ldResult = MA_RW_Latch.getLdResult();
				long aluResult = MA_RW_Latch.getAluResult();
				int instruction = MA_RW_Latch.getInstruction();

				boolean isAluResOverflow = true;
				RegisterFile regFileCopy = containingProcessor.getRegisterFile();

				if((aluResult >>> 32) == 0 || (aluResult >>> 32) == 0x00000000ffffffffL)
					isAluResOverflow = false;

				if(!isIgnore) {
					if(controlSignals.getControlSignal(ControlSignals.OperationSignals.WB.ordinal())) {
						if(isAluResOverflow || controlSignals.getControlSignal(ControlSignals.OperationSignals.DIV.ordinal()))
							regFileCopy.setValue(31, (int) (aluResult >>> 32));

						if(controlSignals.getControlSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
							int rd = (instruction << 10) >>> 27;
							regFileCopy.setValue(rd, ldResult);
							containingProcessor.regLockVector[rd] -= 1;
							containingProcessor.regWriteCurrentCycle[rd] = true;
						}

						else if (controlSignals.getControlSignal(ControlSignals.OperationSignals.IMMEDIATE.ordinal())) {
							int rd = (instruction << 10) >>> 27;
							regFileCopy.setValue(rd, (int) aluResult);
							containingProcessor.regLockVector[rd] -= 1;
							containingProcessor.regWriteCurrentCycle[rd] = true;
						}

						else {
							int rd = (instruction << 15) >>> 27;
							regFileCopy.setValue(rd, (int) aluResult);
							containingProcessor.regLockVector[rd] -= 1;
							containingProcessor.regWriteCurrentCycle[rd] = true;

							if(isAluResOverflow)
								regFileCopy.setValue(31, (int) (aluResult >>> 32));
							containingProcessor.setRegisterFile(regFileCopy);
						}
					}
				}
			}
		}
		MA_RW_Latch.setRW_enable(false);
		EX_MA_Latch.setMA_enable(true);


		System.out.print("RW: ");
		System.out.println(isIgnore);
	}
}
