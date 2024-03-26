package generic;
import java.io.*;
import java.nio.ByteBuffer;

// import java.util.Scanner;

import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue = new EventQueue();

	public static int nInst = 0;
	public static int nStalls = 0;
	public static int nWrong = 0;
	public static long nCycles = 0;

	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		try (InputStream programFileInputStream = new FileInputStream(assemblyProgramFile)) {
			/*
			* 1. load the program into memory according to the program layout described
			*    in the ISA specification
			*/

			byte[] currentInstruction = new byte[4];
			byte[] startPC = new byte[4];
			int byteRead = -1;
			int currentMemoryAddress = 0;

			MainMemory newMainMemory = new MainMemory();
			RegisterFile newRegFile = new RegisterFile();

			//https://stackoverflow.com/questions/2383265/convert-4-bytes-to-int
			//https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html
			//https://www.baeldung.com/java-bytebuffer

			programFileInputStream.read(startPC);

            while ((byteRead = programFileInputStream.read(currentInstruction)) != -1) {
                newMainMemory.setWord(currentMemoryAddress, ByteBuffer.wrap(currentInstruction).getInt());
                currentMemoryAddress += 1;
            }

			/*
			* 2. set PC to the address of the first instruction in the main
			*/

			newRegFile.setProgramCounter(ByteBuffer.wrap(startPC).getInt());

			/*
			* 3. set the following registers:
			*     x0 = 0
			*     x1 = 65535
			*     x2 = 65535
			*/

			newRegFile.setValue(0, 0);
			newRegFile.setValue(1, 65535);
			newRegFile.setValue(2, 65535);

			processor.setMainMemory(newMainMemory);
			processor.setRegisterFile(newRegFile);

		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void simulate()
	{
		processor.enableIFUnit();
		while(simulationComplete == false)
		{
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			processor.afterCycleWork();


			System.out.println("======================================================");
			// Scanner myObj = new Scanner(System.in);  // Create a Scanner object
			// // System.out.println("Enter username");
			// String userName = myObj.nextLine();

			Clock.incrementClock();
			nCycles += 1;
		}
		
		RegisterFile regFileCopy = processor.getRegisterFile();
			int pc = regFileCopy.getProgramCounter();
			regFileCopy.setProgramCounter(pc - 1);
			processor.setRegisterFile(regFileCopy);

		Statistics.setNumberOfInst(nInst);
		Statistics.setNumberOfCycles((int) nCycles);
		Statistics.setNumberOfStalls(nStalls);
		Statistics.setNumberOfWrong(nWrong);
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}

	public static EventQueue getEventQueue() {
		return eventQueue;
	}
}