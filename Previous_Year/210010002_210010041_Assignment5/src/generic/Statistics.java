package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static int datahazards;
	static int controlhazards;

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Number of data hazards = " + datahazards);
			writer.println("Number of control hazards = " + controlhazards);
			
			// TODO add code here to print statistics in the output file
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	// TODO write functions to update statistics

	public static int getNumberOfInstructions() 
	{
		return numberOfInstructions;
	}
	public static int getNumberOfCycles() 
	{
		return numberOfCycles;
	}
	public static void setNumberOfInstructions(int numberOfInstructions) 
	{
		Statistics.numberOfInstructions = numberOfInstructions;
	}
	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public static int getDatahazards() {
		return datahazards;
	}

	public static void setDatahazards(int datahazards) {
		Statistics.datahazards = datahazards;
	}

	public static int getControlhazards() {
		return controlhazards;
	}

	public static void setControlhazards(int controlhazards) {
		Statistics.controlhazards = controlhazards;
	}


}
