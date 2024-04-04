package processor.pipeline;

// import java.time.Clock;
import processor.Clock;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.plaf.synth.SynthStyle;

import generic.Instruction;
import processor.Processor;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;
import generic.Operand;
import generic.Statistics;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	IF_EnableLatchType IF_EnableLatch;
	static OperationType[] opTypes = OperationType.values();
	public boolean Proceed;
	Queue<Integer> queue;
	boolean isEnd;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch,
			IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		isEnd = false;
		Proceed = true;
		queue = new LinkedList<>();
		queue.add(-1);
		queue.add(-1);
		queue.add(-1);
		queue.add(-1);
		queue.add(-1);
	}

	boolean checkdatahazard(int[] operands) {
		for (int i = 0; i < operands.length; i++) {
			if (queue.contains(operands[i])) {
				return true;
			}
		}
		return false;
	}

	void updateQueue(int operand) {
		queue.poll();
		queue.add(operand);
	}

	public static int twoscompliment(String s) {
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '0') {
				chars[i] = '1';
			} else {
				chars[i] = '0';
			}
		}
		String s1 = new String(chars);
		int num = Integer.parseInt(s1, 2);
		num = num + 1;
		return num;
	}

	public void performOF() {
		// System.out.println("me");
		if (isEnd) {
			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(false);
			return;
		}
		if (OF_EX_Latch.isEX_busy()) {
			IF_OF_Latch.setOF_busy(true);
			return;
		} else {
			IF_OF_Latch.setOF_busy(false);
		}

		int addtoqueue = -1;
		boolean noDataHazard = true;
		if (IF_OF_Latch.isOF_enable() && Proceed && !IF_OF_Latch.isOF_busy()) {
			int instruction = IF_OF_Latch.getInstruction();
			Instruction instr = new Instruction();
			String bin_instr = Integer.toBinaryString(instruction);
			if (bin_instr.length() < 32) {
				int diff = 32 - bin_instr.length();
				String zeros = "";
				for (int i = 0; i < diff; i++) {
					zeros += "0";
				}
				bin_instr = zeros + bin_instr;
			}
			instr.setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter());
			int opcode = Integer.parseInt(bin_instr.substring(0, 5), 2);
			OF_EX_Latch.setopcode(opcode);//#######################################################################
			instr.setOperationType(opTypes[opcode]);
			System.out.println("OF code: " + opcode + " Clock: " + Clock.getCurrentTime());
			if (opcode < 22 && opcode % 2 == 0) {
				// R3 type
				Operand rs1 = new Operand();
				Operand rs2 = new Operand();
				Operand rd = new Operand();
				rs1.setOperandType(Operand.OperandType.Register);
				rs2.setOperandType(Operand.OperandType.Register);
				rd.setOperandType(Operand.OperandType.Register);

				rs1.setValue(Integer.parseInt(bin_instr.substring(5, 10), 2));
				rs2.setValue(Integer.parseInt(bin_instr.substring(10, 15), 2));
				rd.setValue(Integer.parseInt(bin_instr.substring(15, 20), 2));
				if (checkdatahazard(new int[] { rs1.getValue(), rs2.getValue(), rd.getValue() })) {
					noDataHazard = false;
				} else {
					addtoqueue = rd.getValue();

					// System.out.println("rd in OF "+rd.getValue());
					this.OF_EX_Latch.setOp1(this.containingProcessor.getRegisterFile().getValue(rs1.getValue()));
					// System.out.println("sayonara");
					// System.out.println("Op1
					// "+containingProcessor.getRegisterFile().getValue(rs1.getValue())+ " rd
					// "+rs1.getValue());
					this.OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rs2.getValue()));
					// this.OF_EX_Latch.setRd(rd);
					// this.OF_EX_Latch.setRs1(rs1);
					// this.OF_EX_Latch.setRs2(rs2);
					OF_EX_Latch.setInstruction(instr);
					instr.setSourceOperand1(rs1);
					instr.setSourceOperand2(rs2);
					instr.setDestinationOperand(rd);
				}
				// int rs1=Integer.parseInt(inst.substring(5,10),2);
				// int rs2=Integer.parseInt(inst.substring(10,15),2);
				// int rd=Integer.parseInt(inst.substring(15,20),2);
				// System.out.println(rs1+Integer.parseInt(rs1,2));
				// System.out.println(rs1+" "+rs2+" "+rd);
			} 
			else if ((opcode < 22 && opcode % 2 != 0) || opcode == 22 || opcode == 23
					|| (opcode > 24 && opcode < 29)) {
				// R2I type
				// System.out.println("here");
				Operand rs1 = new Operand();
				Operand rd = new Operand();
				rs1.setOperandType(Operand.OperandType.Register);
				rd.setOperandType(Operand.OperandType.Register);
				rs1.setValue(Integer.parseInt(bin_instr.substring(5, 10), 2));
				rd.setValue(Integer.parseInt(bin_instr.substring(10, 15), 2));
				// System.out.println("rd in OF "+rd.getValue());
				int imx = Integer.parseInt(bin_instr.substring(15, 32), 2);
				// System.out.println("hello "+ imx);
				if (bin_instr.charAt(15) == '1') {
					imx = -1 * twoscompliment(bin_instr.substring(15, 32));
					// System.out.println("h");
					// System.out.println(imx+" immediate");
				}
				if (checkdatahazard(new int[] { rs1.getValue(), rd.getValue() })) {
					// System.out.println("check_haz");
					noDataHazard = false;
				} else {
					if (opcode >= 23) {
						this.OF_EX_Latch.setImm(imx);
						// this.OF_EX_Latch.setRs1(rs1);
						// this.OF_EX_Latch.setRd(rd);
						OF_EX_Latch.setInstruction(instr);
						instr.setSourceOperand1(rs1);
						// instr.setSourceOperand2(rs2);
						instr.setDestinationOperand(rd);
						this.OF_EX_Latch.setOp1(this.containingProcessor.getRegisterFile().getValue(rs1.getValue()));
						this.OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd.getValue()));
					} else {
						// System.out.println("Lets just get it over");
						addtoqueue = rd.getValue();
						this.OF_EX_Latch.setImm(imx);
						OF_EX_Latch.setInstruction(instr);
						instr.setSourceOperand1(rs1);
						// instr.setSourceOperand2(rs2);
						instr.setDestinationOperand(rd);
						this.OF_EX_Latch.setOp1(this.containingProcessor.getRegisterFile().getValue(rs1.getValue()));
						this.OF_EX_Latch.setOp2(this.containingProcessor.getRegisterFile().getValue(rd.getValue()));
					}
					// this.OF_EX_Latch.setImmx(imx);
					// if(code>24 && code<29) {
					//
					// }
					// System.out.println(rs1+Integer.parseInt(rs1,2));
					// System.out.println(rs1+" "+rd+" "+imx);
				}
			} 
			else if (opcode == 24) {
				// RI type
				Operand op = new Operand();
				String imm = bin_instr.substring(10, 32);
				int imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm_val = -1 * twoscompliment(imm);
				}
				if (imm_val != 0) {
					op.setOperandType(Operand.OperandType.Immediate);
					op.setValue(imm_val);
					instr.setSourceOperand1(op);
				} 
				else {
					op.setOperandType(Operand.OperandType.Register);
					op.setValue(Integer.parseInt(bin_instr.substring(5, 10), 2));
					instr.setSourceOperand1(op);
				}
				if (checkdatahazard(new int[] { op.getValue() })) {
						noDataHazard = false;
					}
				else {
						OF_EX_Latch.setInstruction(instr);
						OF_EX_Latch.setImm(imm_val);
					}
				
				// this.OF_EX_Latch.setImmx(imm_val);
				// this.OF_EX_Latch.setRs1(op);

			} else {
				Operand rd = new Operand();
				rd.setOperandType(Operand.OperandType.Register);
				rd.setValue(Integer.parseInt(bin_instr.substring(5, 10), 2));
				// this.OF_EX_Latch.setRd(rd);
				instr.setDestinationOperand(rd);
				OF_EX_Latch.setInstruction(instr);

				// int imm = Integer.parseInt(bin_instr.substring(10, 32), 2); // TODO: 2's
				// complement
				// if (inst.charAt(10) == '1') {
				// imm = -1 * twoscompliment(bin_instr.substring(10, 32));
				// // System.out.println(inst);
				// }
				// // System.out.println("imm: " + imm);
				// // OF_EX_Latch.setInstruction(inst);
				// OF_EX_Latch.setImmx(imm);
				// this.OF_EX_Latch.setRd(rd);
			}

			OF_EX_Latch.setEX_enable(noDataHazard);
			if (!noDataHazard) {
				System.out.println("Datahazard: " + queue);
				IF_EnableLatch.setFreeze(true);
				Statistics.setDatahazards(Statistics.getDatahazards() + 1);
			}

			updateQueue(addtoqueue);
		} else if (!Proceed) {
			OF_EX_Latch.setEX_enable(false);
		} else {
			OF_EX_Latch.setEX_enable(false);
		}
	}

	public void setisEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public void setProceed(boolean proceed) {
		Proceed = proceed;
		if (!Proceed) {
			OF_EX_Latch.setEX_enable(false);
		}
	}

}
