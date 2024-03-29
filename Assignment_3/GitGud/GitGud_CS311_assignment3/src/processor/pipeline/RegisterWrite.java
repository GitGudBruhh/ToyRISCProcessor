package processor.pipeline;

import generic.Simulator;
import processor.Processor;

//DEBUG WARNING
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.*;

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

		// System.out.println("BEFORE RW");
		// controlSignals.display();
		// System.out.println("==============================================================================");

		String printStr = controlSignals.display();

		if(MA_RW_Latch.isRW_enable()) {
			if(!controlSignals.getControlSignal(ControlSignals.OperationSignals.END.ordinal())) {
				int pc = MA_RW_Latch.getPc();
				int ldResult = MA_RW_Latch.getLdResult();
				long aluResult = MA_RW_Latch.getAluResult();
				int instruction = MA_RW_Latch.getInstruction();

				boolean isAluResOverflow = true;
				RegisterFile regFileCopy = containingProcessor.getRegisterFile();

				if((aluResult >>> 32) == 0 || (aluResult >>> 32) == 0x00000000ffffffffL)
					isAluResOverflow = false;
				// int opcode = instruction >>> 27;

				if(controlSignals.getControlSignal(ControlSignals.OperationSignals.WB.ordinal())) {
					if(isAluResOverflow || controlSignals.getControlSignal(ControlSignals.OperationSignals.DIV.ordinal()))
						regFileCopy.setValue(31, (int) (aluResult >>> 32));

					if(controlSignals.getControlSignal(ControlSignals.OperationSignals.LOAD.ordinal())) {
						int rd = (instruction << 10) >>> 27;
						regFileCopy.setValue(rd, ldResult);
					}

					else if (controlSignals.getControlSignal(ControlSignals.OperationSignals.IMMEDIATE.ordinal())) {
						int rd = (instruction << 10) >>> 27;
						regFileCopy.setValue(rd, (int) aluResult);
					}

					else {
						int rd = (instruction << 15) >>> 27;
						regFileCopy.setValue(rd, (int) aluResult);
						if(isAluResOverflow)
							regFileCopy.setValue(31, (int) (aluResult >>> 32));
						containingProcessor.setRegisterFile(regFileCopy);
					}
				}
				//DEBUG WARNING
				// String textToAppend = String.valueOf(instruction);
				String fileName = "fromA3.txt";

				try(FileWriter fw = new FileWriter(fileName, true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
				{
					out.println(printStr);
					//more code
					// out.println();
					//more code
					out.close();
				} catch (IOException e) {
					//exception handling left as an exercise for the reader
					System.out.println("EXCEPTION");
				}
				//DEBUG WARNING
			}
		}
		MA_RW_Latch.setRW_enable(false);
		IF_EnableLatch.setIF_enable(true);
	}
}
