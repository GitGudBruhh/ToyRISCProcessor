package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	EX_MA_LatchType EX_MA_Latch;
	Interlocks interlocks;
	
	public RegisterWrite(Processor containingProcessor,
	MA_RW_LatchType mA_RW_Latch,
	IF_EnableLatchType iF_EnableLatch,
	EX_MA_LatchType eX_MA_Latch,
	Interlocks interlocks) {
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.EX_MA_Latch = eX_MA_Latch; // USED ONLY FOR TURNING ON THE STAGE
		this.interlocks = interlocks;

	}
	
	public void performRW()
	{
		//TODO
		ControlSignals controlSignals = MA_RW_Latch.getControlSignals();

		/*
        ===================================================================================================
        Stall if control signals have an IGNORE signal
        ===================================================================================================
        */
		// if(controlSignals.getMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal())) {
  //           MA_RW_Latch.setRW_enable(false);
  //           return;
  //       }

		if(MA_RW_Latch.isRW_enable()) {
			if(!controlSignals.getOperationSignal(ControlSignals.OperationSignals.END.ordinal())) {
				int pc = MA_RW_Latch.getPc();
				int ldResult = MA_RW_Latch.getLdResult();
				long aluResult = MA_RW_Latch.getAluResult();
				int instruction = MA_RW_Latch.getInstruction();

				boolean isAluResOverflow = true;
				RegisterFile regFileCopy = containingProcessor.getRegisterFile();

				if((aluResult >>> 32) == 0 || (aluResult >>> 32) == 0x00000000ffffffffL)
					isAluResOverflow = false;
				// int opcode = instruction >>> 27;

				if(controlSignals.getMiscSignal(ControlSignals.MiscSignals.WB.ordinal())) {
					int rd;

					if(isAluResOverflow || controlSignals.getOperationSignal(ControlSignals.OperationSignals.DIV.ordinal()))
						regFileCopy.setValue(31, (int) (aluResult >>> 32));

					else if(controlSignals.getOperationSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
						rd = (instruction << 10) >>> 27;
						regFileCopy.setValue(rd, ldResult);
					}

					else if (controlSignals.getMiscSignal(ControlSignals.MiscSignals.IMMEDIATE.ordinal())) {
						rd = (instruction << 10) >>> 27;
						regFileCopy.setValue(rd, (int) aluResult);
					}

					else {
						rd = (instruction << 15) >>> 27;
						regFileCopy.setValue(rd, (int) aluResult);
						if(isAluResOverflow)
							regFileCopy.setValue(31, (int) (aluResult >>> 32));
						containingProcessor.setRegisterFile(regFileCopy);
					}

					DataInterlock dI_buf = interlocks.getDataInterlockBuf();
					dI_buf.setRegisterLock(rd, false);
					interlocks.setDataInterlockBuf(dI_buf);
				}
			}
		}
		MA_RW_Latch.setRW_enable(false);
		// IF_EnableLatch.setIF_enable(true);
		EX_MA_Latch.setMA_enable(true);
	}
}