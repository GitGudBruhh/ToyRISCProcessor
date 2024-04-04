package processor.pipeline;
import processor.Processor;
import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.ExecutionCompleteEvent;
import generic.Instruction;
import generic.Simulator;
import generic.Statistics;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;

public class Execute implements Element{
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		if(OF_EX_Latch.isEX_enable()&& !OF_EX_Latch.isEX_busy())
		{

			OperationType alu_op = OF_EX_Latch.getInstruction().getOperationType();
			long latency = 0;
			switch (alu_op) {
			case mul:
			case muli:
				latency = Configuration.multiplier_latency;
				break;
			case div:
			case divi:
				latency = Configuration.divider_latency;
				break;
			default:
				latency = Configuration.ALU_latency;
				break;
			}

			Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(latency, this, this));
			OF_EX_Latch.setEX_busy(true);
			EX_MA_Latch.setMA_enable(false);
		}

	}

	@Override
	public void handleEvent(Event e) {

		if(EX_MA_Latch.isMA_busy()){
			e.setEventTime(e.getEventTime()+1);
			Simulator.getEventQueue().addEvent(e);
			return;
		}

		int op1 = OF_EX_Latch.getOp1();
		int op2 = OF_EX_Latch.getOp2();
		int imm = OF_EX_Latch.getImm();
		System.out.println("op1: "+op1+" op2: "+op2+" imm: "+imm);
		Instruction instruction = OF_EX_Latch.getInstruction();
		int cur_pc = containingProcessor.getRegisterFile().getProgramCounter();
		int alu_result = 0;
		System.out.println("EX: " + instruction);
		OperationType alu_op = OF_EX_Latch.getInstruction().getOperationType();
		System.out.println("ALU OP: " + alu_op);
		boolean noma = false;
		int opcode = OF_EX_Latch.getopcode();
		EX_MA_Latch.setopcode(opcode);
		switch(opcode)
		{
			case 0: alu_result = op1 + op2; break;
			case 1: alu_result = op1 + imm; break;
			case 2: alu_result = op1 - op2; break;
			case 3: alu_result = op1 - imm; break;
			case 4: alu_result = op1 * op2; break;
			case 5: alu_result = op1 * imm; break;
			case 6: 
			alu_result = op1 / op2;
			containingProcessor.getRegisterFile().setValue(31, op1 % op2);
			break;
			case 7: 
			alu_result = op1 / imm; 
			containingProcessor.getRegisterFile().setValue(31, op1 % imm);
			break;
			case 8: alu_result = op1 & op2; break;
			case 9: alu_result = op1 & imm; break;
			case 10: alu_result = op1 | op2; break;
			case 11: alu_result = op1 | imm; break;
			case 12: alu_result = op1 ^ op2; break;
			case 13: alu_result = op1 ^ imm; break;
			case 14: alu_result= (op1 < op2) ? 1 : 0; break;
			case 15: alu_result= (op1 < imm) ? 1 : 0; break;
			case 16:
			containingProcessor.getRegisterFile().setValue(31, (int) Math.pow(2, op2));
			alu_result = op1 << op2;
			break;
			case 17: 
			containingProcessor.getRegisterFile().setValue(31, (int) Math.pow(2, imm));
			alu_result = op1 << imm;
			break;
			case 18:
			containingProcessor.getRegisterFile().setValue(31, op1 & (1 << (op2 - 1)));
			alu_result = op1 >>> op2;
			break;
			case 19:
			containingProcessor.getRegisterFile().setValue(31, op1 & (1 << (imm - 1)));
			alu_result = op1 >>> imm;
			break;
			case 20:
			containingProcessor.getRegisterFile().setValue(31, op1 & (1 << (op2 - 1)));
			alu_result = op1 >> op2;
			break;
			case 21:
			containingProcessor.getRegisterFile().setValue(31, op1 & (1 << (imm - 1)));
			alu_result = op1 >> imm;
			break;

			case 22: alu_result = op1 + imm; break;
			case 23: alu_result = op2 + imm; break;
			case 24:
			{
				OperandType optype = instruction.getSourceOperand1().getOperandType();
				if (optype == OperandType.Register){
					imm = containingProcessor.getRegisterFile().getValue(
						instruction.getSourceOperand1().getValue());
					}
				else{
					imm = OF_EX_Latch.getImm();
					}
				alu_result = cur_pc + imm ;
				EX_IF_Latch.setIF_enable(true);
				
				EX_IF_Latch.setPC(alu_result-1);
				noma = true;
				containingProcessor.getOFUnit().setProceed(false);
			}
			break;
			case 25:
			{
				if(op1 == op2)
				{
					EX_IF_Latch.setIF_enable(true);
					alu_result = cur_pc + imm;
					EX_IF_Latch.setPC(alu_result-1);
					noma = true;
					containingProcessor.getOFUnit().setProceed(false);
				}
			}
			break;
			case 26:
			{
				if(op1 != op2)
				{
					alu_result = cur_pc + imm;
					EX_IF_Latch.setIF_enable(true);
					EX_IF_Latch.setPC(alu_result-1);
					noma = true;
					containingProcessor.getOFUnit().setProceed(false);
				}
			}
			break;
			case 27:
			{

				if(op1 < op2)
				{
					alu_result = cur_pc + imm;
					EX_IF_Latch.setIF_enable(true);
					EX_IF_Latch.setPC(alu_result-1);
					noma = true;
					containingProcessor.getOFUnit().setProceed(false);
				}
			}
			break;
			case 28:
			{
				if(op1 > op2)
				{
					alu_result = cur_pc + imm;
					EX_IF_Latch.setIF_enable(true);
					EX_IF_Latch.setPC(alu_result-1);
					noma = true;
					containingProcessor.getOFUnit().setProceed(false);
				}
			}
			break;
			case 29:
			{
				containingProcessor.getOFUnit().setisEnd(true);
				break;
			}
			default:
				break;
		}

		System.out.println("ALU RESULT: " + alu_result+"\n\n");

		EX_MA_Latch.setALUResult(alu_result);
		EX_MA_Latch.setInstruction(OF_EX_Latch.getInstruction());
		if(!noma)
		{
			EX_MA_Latch.setMA_enable(true);
		}else{
			Statistics.setControlhazards(Statistics.getControlhazards()+2); // stall 2 cycles
		}
		OF_EX_Latch.setEX_busy(false);
	}
}
