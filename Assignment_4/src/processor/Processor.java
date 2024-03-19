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
import processor.pipeline.Interlocks;

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

	Interlocks interlocks;
	boolean isIdle = true;
	
	public Processor()
	{
		registerFile = new RegisterFile();
		mainMemory = new MainMemory();
		interlocks = new Interlocks();
		
		IF_EnableLatch = new IF_EnableLatchType();
		IF_OF_Latch = new IF_OF_LatchType();
		OF_EX_Latch = new OF_EX_LatchType();
		EX_MA_Latch = new EX_MA_LatchType();
		EX_IF_Latch = new EX_IF_LatchType();
		MA_RW_Latch = new MA_RW_LatchType();
		
		IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch, MA_RW_Latch, interlocks);
		OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch, IF_EnableLatch, interlocks);
		EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch, IF_OF_Latch, interlocks);
		MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch, OF_EX_Latch, interlocks);
		RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch, EX_MA_Latch, interlocks);
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

	public Interlocks getInterlocks() {
		return interlocks;
	}

	public boolean isIdle() {
		return isIdle;
	}

	public void setIdle(boolean isIdle) {
		this.isIdle = isIdle;
	}

	public void writeInterlockBuffer() {
		this.interlocks.writeBufferContents();
		return;
	}

	public void setNops() {
		ControlInterlock cInterlock = this.interlocks.readControlInterlock();
		DataInterlock dInterlock = this.interlocks.readDataInterlock();


		if(cInterlock.isBranchInExTaken()) {
			// ControlSignals cSignals_OF = IF_OF_Latch.setIgnore(true);
			ControlSignals cSignals_EX = OF_EX_Latch.getControlSignals();

			cSignals_OF.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal());
			cSignals_IF.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal());

			//TODO:
			// +2 for wrong branch
		}
	}
}
