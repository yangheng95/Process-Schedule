package com.yangheng.OS.Model;

import com.yangheng.OS.Main.ProcessSchedule;

public class Instruction {

	private String InstructionName;
	private int RunTime = 0;// 运行时间
	private int TimeRemain = 0;// 指令剩余运行时间
	public Instruction former = null;
	public Instruction next = null;

	public Instruction(String instructionName, int runTime) {
		this.InstructionName = instructionName;
		this.RunTime = runTime;
	}

	public String getInstructionName() {
		return InstructionName;
	}

	public void setInstructionName(String instructionName) {
		InstructionName = instructionName;
	}

	public int getRunTime() {
		return RunTime;
	}

	public void setRunTime(int runTime) {
		RunTime = runTime;
		if (RunTime < 0) {
			if (InstructionName.equals("C")) {
				ProcessSchedule.start += RunTime;
				ProcessSchedule.realruntime = ProcessSchedule.timeship+RunTime;
			}
			RunTime = 0;
		}
		else ProcessSchedule.realruntime = ProcessSchedule.timeship;
	}

	public int getTimeRemain() {
		return TimeRemain;
	}

	public void setTimeRemain(int timeRemain) {
		TimeRemain = timeRemain;
	}

	public Instruction getFormer() {
		return former;
	}

	public void setFormer(Instruction former) {
		this.former = former;
	}

	public Instruction getNext() {
		return next;
	}

	public void setNext(Instruction next) {
		this.next = next;
	}

	public boolean append(Instruction instruction) {

		try {
			this.next = instruction;
			instruction.former = this;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public String toString() {

		try {
			return "Instruction [InstructionName=" + InstructionName + ", RunTime=" + RunTime + ", TimeRemain="
					+ TimeRemain + ", former=" + former.InstructionName + ", next=" + next.InstructionName + "]";
		} catch (Exception e) {
			try {
				return "Instruction [InstructionName=" + InstructionName + ", RunTime=" + RunTime + ", TimeRemain="
						+ TimeRemain + ", former=null" + ", next=" + next.InstructionName + "]";
			} catch (Exception e1) {
				try {
					return "Instruction [InstructionName=" + InstructionName + ", RunTime=" + RunTime + ", TimeRemain="
							+ TimeRemain + ", former=" + former.InstructionName + ", next=null" + "]";
				} catch (Exception e2) {
					return "Instruction [InstructionName=" + InstructionName + ", RunTime=" + RunTime + ", TimeRemain="
							+ TimeRemain + ", former=null" + ", next=null" + "]";
				}
			}
		}
	}

}
