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

				for(int idx = 0; idx < dataArrayListSize; idx++) {
					Integer currentData;
					currentData = ParsedProgram.data.get(idx); //Returns one line of data as Integer

					bytesToWrite = ByteBuffer.allocate(4).putInt(currentData.intValue()).array();
					oStreamObjProgFile.write(bytesToWrite);
				}


		//============================================================
		//4. assemble one instruction at a time, and write to the file
		//============================================================
				int codeArrayListSize = ParsedProgram.code.size();

				for(int idx = 0; idx < codeArrayListSize; idx++) {
					Instruction currentInstruction;
					currentInstruction = ParsedProgram.code.get(idx); //Returns one line of data as Instruction

					Instruction.OperationType currentOperationType = currentInstruction.getOperationType();
					Operand currentSourceOpnd1 = currentInstruction.getSourceOperand1();
					Operand currentSourceOpnd2 = currentInstruction.getSourceOperand2();
					Operand currentDestOpnd = currentInstruction.getDestinationOperand();

					Operand.OperandType opndTypeRs1;
					Operand.OperandType opndTypeRs2;
					Operand.OperandType opndTypeRd;

					opndTypeRs1 = (currentSourceOpnd1 == null) ? null : currentSourceOpnd1.getOperandType();
					opndTypeRs2 = (currentSourceOpnd2 == null) ? null : currentSourceOpnd2.getOperandType();
					opndTypeRd = (currentDestOpnd == null) ? null : currentDestOpnd.getOperandType();

					//For any arithmetic operation, imm values/label are kept in sourceOpnd2
					//Opcodes: 0 -> 21 (Odd no.s)

					//For loads and stores, imm values/label
					//imm values are kept in sourceOpnd2
					//Opcodes: 22, 23

					//In jmp statements, either imm/label = 0, or rd = 0.
					//The non zero value is kept in destOpnd
					//Opcode: 24

					//In conditional branches, sourceOpnd1 is compared to sourceOpnd2; immediate/label
					//is in	destOpnd.
					//Opcodes: 25 -> 28

					//In end instruction, rd and imm are unused
					//Opcode: 29

					//So, imm values (as labels), can be in destOpnd, sourceOpnd2, but never sourceOpnd1

					int currentInstructionAsInt = 0;

					//Convert operation to opcode(int) and shift left
					currentInstructionAsInt += (currentOperationType.ordinal() << 27);

					//currentInstructionAsInt += currentSourceOpnd1.getValue() << 22;
					//currentInstructionAsInt += currentSourceOpnd2.getValue() << 17;

					//RI type (jmp & end)
					if(currentSourceOpnd1 == null) {

						//jmp
						if(currentInstruction.getOperationType() == Instruction.OperationType.jmp) {

							//Label
							if(opndTypeRd == Operand.OperandType.Label) {
								Integer addrOfLabel = ParsedProgram.symtab.get(currentDestOpnd.getLabelValue());
								Integer valAtLabel = ParsedProgram.data.get(addrOfLabel.intValue());
								currentInstructionAsInt += valAtLabel.intValue();
							}

							//Immediate
							else if(opndTypeRd == Operand.OperandType.Immediate) {
								currentInstructionAsInt += currentDestOpnd.getValue();
							}

							//Register
							else
								currentInstructionAsInt += (currentDestOpnd.getValue() << 22);

						}

						//end
						// else if(currentInstruction.getOperationType() == Instruction.OperationType.end) {
						// 	currentInstructionAsInt += 0;
						// }
					}

					//R3 or R2I type
					else {

						Integer addrOfLabel;
						Integer valAtLabel;

						//Label to addrOfLabel to valAtLabel
						if(opndTypeRs2 == Operand.OperandType.Label) {
							addrOfLabel = ParsedProgram.symtab.get(currentSourceOpnd2.getLabelValue());
							valAtLabel = ParsedProgram.data.get(addrOfLabel.intValue());
						}

					}


					bytesToWrite = ByteBuffer.allocate(4).putInt(currentInstructionAsInt).array();
					oStreamObjProgFile.write(bytesToWrite);
				}

		//============================================================
		//5. close the file
		//============================================================

				oStreamObjProgFile.close();
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
