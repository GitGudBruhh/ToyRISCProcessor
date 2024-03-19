package generic;
import java.io.*;
import java.nio.ByteBuffer;

import java.util.concurrent.*;
import java.util.Scanner;

import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
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
			* TODO
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
		int numberOfInstructionsExecuted = 0;
		long numberOfCycles = 0;
		boolean lastCycle = false;

		processor.enableIFUnit();
		while(simulationComplete == false)
		{

			System.out.println(numberOfCycles);
			System.out.println();

			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			processor.getOFUnit().performOF();

			if(lastCycle)
				processor.forceDisableIFUnit();

			processor.getIFUnit().performIF();
			// System.out.println("WHAT");

			if(processor.isIdle()) {
				if(!lastCycle)
				{
					lastCycle = true;
					// System.out.println("WHAAAT");
					continue;
				}
				else {
					simulationComplete = true;
					break;
				}
			}
			processor.afterCycleRun();
			Clock.incrementClock();
			// try {
			// TimeUnit.SECONDS.sleep(1);
			// }
			// catch(InterruptedException e) {
			// 	System.out.println("BRUH");
			// }

			// processor.printState(0, 30);

			// Scanner myObj = new Scanner(System.in);
			// System.out.println("Go to next step");
			// String userName = myObj.nextLine();

			numberOfInstructionsExecuted += 1;
			numberOfCycles += 1;
		}
		
		// TODO
		// set statistics
		numberOfCycles = Clock.getCurrentTime();

		Statistics.setNumberOfInstructions(numberOfInstructionsExecuted);
		Statistics.setNumberOfCycles((int) numberOfCycles);
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
