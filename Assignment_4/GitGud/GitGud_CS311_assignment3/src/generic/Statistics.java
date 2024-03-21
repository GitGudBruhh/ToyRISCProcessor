package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static int numberOfInst;
	static int numberOfWrong;
	static int numberOfStalls;
	
	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			// writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Number of wrong entries = " + numberOfWrong);
			writer.println("Number of stalls = " + numberOfStalls);
			// writer.println("Number of cycles taken = " + numberOfCycles);
			
			// TODO add code here to print statistics in the output file
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public static void setNumberOfInst(int nInst) {
		Statistics.numberOfInst = nInst;
	}

	public static void setNumberOfWrong(int nWrong) {
		Statistics.numberOfWrong = nWrong;
	}

	public static void setNumberOfStalls(int nStalls) {
		Statistics.numberOfStalls = nStalls;
	}
}
