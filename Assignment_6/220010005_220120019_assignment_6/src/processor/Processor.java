package processor;

import processor.memorysystem.MainMemory;
import processor.memorysystem.Cache;
import processor.pipeline.EX_IF_LatchType;
import processor.pipeline.EX_MA_LatchType;
import processor.pipeline.Execute;
import processor.pipeline.IF_EnableLatchType;
import processor.pipeline.IF_OF_LatchType;
import processor.pipeline.InstructionFetch;
import processor.pipeline.MA_RW_LatchType;
import processor.pipeline.MemoryAccess;
import processor.pipeline.OF_EX_LatchType;
import processor.pipeline.OperandFetch;
import processor.pipeline.RegisterFile;
import processor.pipeline.RegisterWrite;
import generic.Simulator;
import configuration.Configuration;

public class Processor {
	
	RegisterFile registerFile;
	MainMemory mainMemory;
	
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	InstructionFetch IFUnit;
	OperandFetch OFUnit;
	Execute EXUnit;
	MemoryAccess MAUnit;
	RegisterWrite RWUnit;

	Cache L1i_Cache;
	Cache L1d_Cache;

	// boolean isIdle = true;

	public int[] regLockVector;
	public int[] regWrite;
	public boolean branchTakenCurrentCycle;
	
	public Processor()
	{
		registerFile = new RegisterFile();
		mainMemory = new MainMemory();
		
		IF_EnableLatch = new IF_EnableLatchType();
		IF_OF_Latch = new IF_OF_LatchType();
		OF_EX_Latch = new OF_EX_LatchType();
		EX_MA_Latch = new EX_MA_LatchType();
		EX_IF_Latch = new EX_IF_LatchType();
		MA_RW_Latch = new MA_RW_LatchType();
		
		IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch);
		OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch, IF_EnableLatch);
		EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch, IF_EnableLatch, IF_OF_Latch);
		MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch);
		RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch);

		L1i_Cache = new Cache(this, 4, Configuration.L1i_numberOfLines);
		L1d_Cache = new Cache(this, 4, Configuration.L1d_numberOfLines);

		regLockVector = new int[32];
		regWrite = new int[32];
		branchTakenCurrentCycle = false;
	}
	
	public void printState(int memoryStartingAddress, int memoryEndingAddress)
	{
		System.out.println(registerFile.getContentsAsString());
		
		System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));		
	}

	public RegisterFile getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public MainMemory getMainMemory() {
		return mainMemory;
	}

	public Cache getL1i_Cache() {
		return L1i_Cache;
	}

	public Cache getL1d_Cache() {
		return L1d_Cache;
	}

	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}

	public void enableIFUnit() {
		this.IF_EnableLatch.setIF_enable(true);
	}

	public InstructionFetch getIFUnit() {
		return IFUnit;
	}

	public OperandFetch getOFUnit() {
		return OFUnit;
	}

	public Execute getEXUnit() {
		return EXUnit;
	}

	public MemoryAccess getMAUnit() {
		return MAUnit;
	}

	public RegisterWrite getRWUnit() {
		return RWUnit;
	}

	public void afterCycleWork() {

		if(branchTakenCurrentCycle)
			Simulator.nWrong += 2;

		// for(int i = 0; i < 32; i++)
		// 	System.out.print(regLockVector[i]+" ");
		// System.out.println();
		// for(int i = 0; i < 32; i++)
		// 	System.out.print(regWrite[i]+" ");
		// System.out.println();

		branchTakenCurrentCycle = false;

		for (int i = 0; i < 32; i++)
			regWrite[i] = 0;

		EX_IF_Latch.writeBuffer();

		// System.out.println("IF STAGE");
		// System.out.println(IF_OF_Latch.getInstruction());
		// System.out.println();

		// System.out.println("OF STAGE");
		// OF_EX_Latch.getControlSignals().display();
		// System.out.println();

		// System.out.println("EX STAGE");
		// EX_IF_Latch.getControlSignals().display();
		// EX_MA_Latch.getControlSignals().display();
		// System.out.println();

		// System.out.println("MA STAGE");
		// MA_RW_Latch.getControlSignals().display();
		// System.out.println();

		// System.out.println(IF_EnableLatch.isIF_busy() + " " + IF_OF_Latch.isOF_busy() + " " + OF_EX_Latch.isEX_busy() + " " + EX_MA_Latch.isMA_busy());
		// System.out.println();
		// System.out.println(IF_EnableLatch.isIF_enable() + " " + IF_OF_Latch.isOF_enable() + " " + OF_EX_Latch.isEX_enable() + " " + EX_MA_Latch.isMA_enable());

		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		IF_OF_Latch.display();
		OF_EX_Latch.display();
		EX_IF_Latch.display();
		EX_MA_Latch.display();
		MA_RW_Latch.display();
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}
}
