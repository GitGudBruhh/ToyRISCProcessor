package generic;

// import java.io.FileInputStream;
// import java.io.FileOutputStream;
import java.io.*;
import java.nio.*;
import generic.Operand.OperandType;


public class Simulator {
		
	static FileInputStream inputcodeStream = null;
	static int fcAddress;
	
	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		fcAddress = firstCodeAddress; //Custom Addition: Set variable in class to firstCodeAddress. Will be used to write into file
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	// private static boolean isImmediate()
	
	public static void assemble(String objectProgramFile)
	{
		//TODO your assembler code

		//============================================================
		//1. open the objectProgramFile in binary mode
		//============================================================

		try {
			FileOutputStream oStreamObjProgFile = new FileOutputStream(objectProgramFile);

			try {
		//============================================================
		//2. write the firstCodeAddress to the file
		//============================================================


				//REFER TO THIS: 	https://stackoverflow.com/questions/2183240/java-integer-to-byte-array
				//					https://docs.oracle.com/javase/6/docs/api/java/nio/ByteBuffer.html

				byte[] bytesToWrite = ByteBuffer.allocate(4).putInt(fcAddress).array();
					oStreamObjProgFile.write(bytesToWrite);


		//============================================================
		//3. write the data to the file
		//============================================================
			int dataArrayListSize = ParsedProgram.data.size();

			for(int idx = 0; idx < dataArrayListSize; idx++)
			{
				Integer currentData;
				currentData = ParsedProgram.data.get(idx); //Returns one line of data as Integer

				bytesToWrite = ByteBuffer.allocate(4).putInt(currentData.intValue()).array();
					oStreamObjProgFile.write(bytesToWrite);
			}


		//============================================================
		//4. assemble one instruction at a time, and write to the file
		//============================================================
			int codeArrayListSize = ParsedProgram.code.size();

			for(int idx = 0; idx < codeArrayListSize; idx++)
			{
				Instruction currentInstruction;
				currentInstruction = ParsedProgram.code.get(idx); //Returns one line of data as Instruction

				OperationType currentOperationType = currentInstruction.getOperationType();
				Operand currentSourceOpnd1 = currentInstruction.getSourceOperand1();
				Operand currentSourceOpnd2 = currentInstruction.getSourceOperand2();
				Operand currentDestOpnd = currentInstruction.getDestinationOperand();

				int currentInstructionAsInt = 0;

				//Convert operation to opcode(int) and shift left
				currentInstructionAsInt += currentOperationType.ordinal() << 27;

				//Checkout jmp, outlier
				if(currentSourceOpnd1.getOperandType().ordinal() == 0 && currentSourceOpnd1.ge)
				currentInstructionAsInt += currentSourceOpnd1 << 22;
				/*
				For any operand no. <= 21, all odd ones are immediate type, all even are register type.
				*/


				bytesToWrite = ByteBuffer.allocate(4).putInt(currentInstructionAsInt.intValue()).array();
					oStreamObjProgFile.write(bytesToWrite);
			}

		//============================================================
		//5. close the file
		//============================================================

			}
			catch (IOException e) {
				System.out.print("File Write Exception");
			}
		}
		catch(FileNotFoundException e) {
			Misc.printErrorAndExit(e.toString());
		}
	}
	
}
