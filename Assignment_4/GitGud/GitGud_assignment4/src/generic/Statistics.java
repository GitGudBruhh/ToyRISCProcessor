package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	// static int numberOfInstructions;
	static int numberOfCycles;
	static int numberOfInst;
	static int numberOfWrong;
	static int numberOfStalls;
	static long currentTime;
	
	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInst);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Current Time = " + currentTime);
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
	// public static void setNumberOfInstructions(int numberOfInstructions) {
	// 	Statistics.numberOfInstructions = numberOfInstructions;
	// }

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public static void setNumberOfInst(int numberOfCycles) {
		Statistics.numberOfInst = numberOfCycles;
	}

	public static void setNumberOfWrong(int numberOfCycles) {
		Statistics.numberOfWrong = numberOfCycles;
	}

	public static void setNumberOfStalls(int numberOfCycles) {
		Statistics.numberOfStalls = numberOfCycles;
	}

	public static void setCurrentTime(long currentTime) {
		Statistics.currentTime = currentTime;
	}
}
