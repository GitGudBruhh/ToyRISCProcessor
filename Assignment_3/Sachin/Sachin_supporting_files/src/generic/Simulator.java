package generic;

import processor.Clock;
import processor.Processor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import generic.Statistics;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}

    // Load the program into memory from the provided assembly program file
    private static void loadProgram(String assemblyProgramFile) {
        try (FileInputStream file = new FileInputStream(assemblyProgramFile);
             BufferedInputStream reader = new BufferedInputStream(file)) {

            int currentPC = 0; // Stores current PC value

            // Read PC value from the first 4 bytes of the file
            byte[] pcBytes = new byte[4];
            if (reader.read(pcBytes) != -1) {
                currentPC = ByteBuffer.wrap(pcBytes).getInt();
                processor.getRegisterFile().setProgramCounter(currentPC);
            }

            int Nd = -1; // Bytes 0 to Nd of memory have global data stored in them;
            int Nt = Nd + 1; // Bytes Nd + 1 to Nt of memory have text/code segment stored in them

            // Writing global data to main memory
            for (int lineCounter = 0; lineCounter < currentPC; lineCounter++) {
                byte[] dataBytes = new byte[4];
                if (reader.read(dataBytes, 0, 4) != -1) {
                    Nd++;
                    int num = ByteBuffer.wrap(dataBytes).getInt();
                    processor.getMainMemory().setWord(Nd, num);
                    Nt = Nd;
                } else {
                    break; // Break the loop if end of file is reached
                }
            }

            // Writing instructions to main memory
            while (true) {
                byte[] instructionBytes = new byte[4];
                if (reader.read(instructionBytes, 0, 4) != -1) {
                    Nt++;
                    int ins = ByteBuffer.wrap(instructionBytes).getInt();
                    processor.getMainMemory().setWord(Nt, ins);
                } else {
                    break; // Break the loop if end of file is reached
                }
            }

            // Set register values
            processor.getRegisterFile().setValue(0, 0);
            processor.getRegisterFile().setValue(1, 65535);
            processor.getRegisterFile().setValue(2, 65535);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Start the simulation
    public static void simulate() {
    	int numberOfInstructions = 0;
	int numberOfCycles = 0;
	

        // Run the simulation until completion
        while (!simulationComplete) {
            processor.getIFUnit().performIF(); // Instruction Fetch stage
            Clock.incrementClock(); // Increment the clock cycle
            processor.getOFUnit().performOF(); // Operand Fetch stage
            Clock.incrementClock(); // Increment the clock cycle
            processor.getEXUnit().performEX(); // Execution stage
            Clock.incrementClock(); // Increment the clock cycle
            processor.getMAUnit().performMA(); // Memory Access stage
            Clock.incrementClock(); // Increment the clock cycle
            processor.getRWUnit().performRW(); // Register Write stage
            Clock.incrementClock(); // Increment the clock cycle

            // Update statistics
           
        }
         Statistics.setNumberOfInstructions(numberOfInstructions);
         Statistics.setNumberOfCycles(numberOfCycles);
    }

    // Set the simulation completion flag
    public static void setSimulationComplete(boolean value) {
        simulationComplete = value;
    }
}