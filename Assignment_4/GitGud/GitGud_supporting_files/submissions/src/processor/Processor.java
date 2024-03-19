package processor;

import processor.memorysystem.MainMemory;
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

	boolean isIdle = true;
	public int[] regLockVector;
	public boolean[] regWriteCurrentCycle;
	boolean branchTakenCurrentCycle;
	boolean isStalled;
	boolean flag_IOE_clear;
	
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
		
		IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch, MA_RW_Latch);
		OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch, IF_EnableLatch);
		EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch, IF_OF_Latch);
		MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch, OF_EX_Latch);
		RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch, EX_MA_Latch);

		regLockVector = new int[32];
		regWriteCurrentCycle = new boolean[32];
		flag_IOE_clear = false;
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

	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}

	public void enableIFUnit() {
		this.IF_EnableLatch.setIF_enable(true);
	}

	public void forceDisableIFUnit() {
		this.IF_EnableLatch.setIF_enable(false);
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

	public boolean isIdle() {
		return isIdle;
	}

	public void setIdle(boolean isIdle) {
		this.isIdle = isIdle;
	}

	public boolean isStalled() {
		return this.isStalled;
	}

	public void setStalled(boolean stalled) {
		this.isStalled = stalled;
	}

	public boolean isBranchTakenCurrentCycle() {
		return this.branchTakenCurrentCycle;
	}

	public void setBranchTakenCurrentCycle(boolean b_taken) {
		this.branchTakenCurrentCycle = b_taken;
	}

	public void afterCycleRun() {

		EX_IF_Latch.writeBuffer();

		for(boolean v : regWriteCurrentCycle)
			v = false;

		branchTakenCurrentCycle = false;

		if(flag_IOE_clear) {
			IF_OF_Latch.setIgnore(true);
			OF_EX_Latch.setIgnore(true);
			EX_MA_Latch.setIgnore(true);
		}
	}

	public void setIOE_clear() {
		flag_IOE_clear = true;
	}
}
