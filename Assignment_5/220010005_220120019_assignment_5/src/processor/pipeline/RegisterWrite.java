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
		ControlSignals controlSignals = MA_RW_Latch.getControlSignals();
		// int pc = MA_RW_Latch.getPc();
		int ldResult = MA_RW_Latch.getLdResult();
		long aluResult = MA_RW_Latch.getAluResult();
		int instruction = MA_RW_Latch.getInstruction();

		if(instruction == 0) {
			return;
		}
		if(MA_RW_Latch.isRW_enable()) {
			System.out.println("WRITING THIS");
			controlSignals.display();

			if(!controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {

				boolean isAluResOverflow = true;
				RegisterFile regFileCopy = containingProcessor.getRegisterFile();

				if((aluResult >>> 32) == 0 || (aluResult >>> 32) == 0x00000000ffffffffL)
					isAluResOverflow = false;
				// int opcode = instruction >>> 27;

				if(controlSignals.getControlSignal(ControlSignals.OperationSignals.WB.ordinal())) {
					if(isAluResOverflow || controlSignals.getControlSignal(ControlSignals.OperationSignals.DIV.ordinal())) {
						containingProcessor.regLockVector[31] -= 1;
						containingProcessor.regWrite[31] = 1;
						regFileCopy.setValue(31, (int) (aluResult >>> 32));
					}

					if(controlSignals.getControlSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
						int rd = (instruction << 10) >>> 27;
						regFileCopy.setValue(rd, ldResult);
						containingProcessor.regLockVector[rd] -= 1;
						containingProcessor.regWrite[rd] = 1;
					}

					else if (controlSignals.getControlSignal(ControlSignals.OperationSignals.IMMEDIATE.ordinal())) {
						int rd = (instruction << 10) >>> 27;
						regFileCopy.setValue(rd, (int) aluResult);
						containingProcessor.regLockVector[rd] -= 1;
						containingProcessor.regWrite[rd] = 1;
					}

					else {
						int rd = (instruction << 15) >>> 27;
						regFileCopy.setValue(rd, (int) aluResult);
						if(isAluResOverflow)
							regFileCopy.setValue(31, (int) (aluResult >>> 32));
						containingProcessor.setRegisterFile(regFileCopy);
						containingProcessor.regLockVector[rd] -= 1;
						containingProcessor.regWrite[rd] = 1;
					}

				Simulator.nInst += 1;
				}
			}
			else {
				Simulator.nInst += 1;
				Simulator.setSimulationComplete(true);

				RegisterFile regFileCopy = containingProcessor.getRegisterFile();
				int tmp_pc = regFileCopy.getProgramCounter();
				regFileCopy.setProgramCounter(tmp_pc+1);
				containingProcessor.setRegisterFile(regFileCopy);
				// System.out.print("SIMCOMPLETE");
			}

			MA_RW_Latch.setNop();
		}
		// MA_RW_Latch.setRW_enable(false);
		// IF_EnableLatch.setIF_enable(true);
	}
}
