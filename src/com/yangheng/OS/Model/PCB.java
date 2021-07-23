package com.yangheng.OS.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.yangheng.OS.Main.OSmain;
import com.yangheng.OS.Main.ProcessSchedule;

public class PCB {
	PCB former;
	PCB next;
	public int ProcessID = 0;
	public String ProcessName;
	public int TimeRemained = 0;
	ArrayList<Instruction> instructions = new ArrayList<>();
	public Instruction CurrentInstruction;
	public int status = 0;
	public String StartTime = "";

	public PCB(int ID, String Name) {
		this.ProcessID = ID;
		this.ProcessName = Name;
		this.status = 2;
		instructions = new ArrayList<>();
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SS");
		long time = date.getTime();
		StartTime = simpleDateFormat.format(time);
	}

	public PCB getFormer() {
		return former;
	}

	public void setFormer(PCB former) {
		this.former = former;
		if (former != null) {
			former.next = this;
		}
	}

	public PCB getNext() {
		return next;
	}

	public void setNext(PCB next) {
		this.next = next;
		if (next != null) {
			next.former = this;
		}
	}

	public ArrayList<Instruction> getInstructions() {
		return instructions;
	}

	public Instruction getCurrentInstruction() {
		return CurrentInstruction;
	}

	public void setCurrentInstruction(Instruction currentInstruction) {
		CurrentInstruction = currentInstruction;
	}

	public boolean addInstruction(Instruction instruction) {
		try {

			for (int i = 0; i < instructions.size(); i++) {
				if (instructions.get(i).next == null) {
					instructions.get(i).append(instruction);
				}
			}
			for (int i = 0; i < instructions.size(); i++) {
				if (instructions.get(i).former == null) {
					CurrentInstruction = instructions.get(i);
					break;
				}
			}	
			this.instructions.add(instruction);
			TimeRemained += instruction.getRunTime();
		} catch (Exception e) {
			System.out.println("添加指令出错！");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void RunOnCpu() {
		CurrentInstruction.setRunTime(CurrentInstruction.getRunTime() - ProcessSchedule.timeship);
		ProcessSchedule.CurrentRunPCB = this;
		if (status == 2) {
			ProcessSchedule.pcb_backupready.remove(this);
		} else if (status == 1) {
			ProcessSchedule.pcb_ready.remove(this);
		}else if (status == 3) {
			ProcessSchedule.pcb_input.remove(this);
		} else if (status == 4) {
			ProcessSchedule.pcb_output.remove(this);
		} else if (status == 5) {
			ProcessSchedule.pcb_wait.remove(this);
		} else if (status == 6) {
			ProcessSchedule.pcb_all.remove(this);
		}
		if (this.next != null) {// 如果被调度的是队列里最后一个进程，那么他将没有nextPCB
			this.next.former = null;
		}
		status = 0;
		OSmain.Log(this.ProcessName + "(ProcessID=" + this.ProcessID + ") 已占用CPU");
		
	}

	public void waitForInput() {
//		this.former = null;
//		this.next = null;
		try {
			ProcessSchedule.pcb_ready.remove(this);
			ProcessSchedule.pcb_backupready.remove(this);
			ProcessSchedule.pcb_input.remove(this);
			ProcessSchedule.pcb_wait.remove(this);
			ProcessSchedule.pcb_output.remove(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProcessSchedule.pcb_input.add(this);
		OSmain.Log(this.ProcessName + " (ProcessID=" + this.ProcessID + ") 进入输入等待队列");
		status = 3;
	}

	public void waitForOutput() {// 从执行状态变成等待输出状态
//		this.former = null;
//		this.next = null;
		try {
			ProcessSchedule.pcb_ready.remove(this);
			ProcessSchedule.pcb_backupready.remove(this);
			ProcessSchedule.pcb_input.remove(this);
			ProcessSchedule.pcb_wait.remove(this);
			ProcessSchedule.pcb_output.remove(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProcessSchedule.pcb_output.add(this);
		OSmain.Log(this.ProcessName + " (ProcessID=" + this.ProcessID + ") 进入输出等待队列");
		status = 4;
	}

	public void Wait() {// 从执行状态变成阻塞态
//		this.former = null;
//		this.next = null;
		try {
			ProcessSchedule.pcb_ready.remove(this);
			ProcessSchedule.pcb_backupready.remove(this);
			ProcessSchedule.pcb_input.remove(this);
			ProcessSchedule.pcb_wait.remove(this);
			ProcessSchedule.pcb_output.remove(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProcessSchedule.pcb_wait.add(this);
		OSmain.Log(this.ProcessName + " (ProcessID=" + this.ProcessID + ") 进入其他等待队列");
		status = 5;
	}

	public void remove() {
//		this.former = null;
//		this.next = null;
		try {
			ProcessSchedule.pcb_ready.remove(this);
			ProcessSchedule.pcb_backupready.remove(this);
			ProcessSchedule.pcb_input.remove(this);
			ProcessSchedule.pcb_wait.remove(this);
			ProcessSchedule.pcb_output.remove(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProcessSchedule.CurrentRunPCB = null;
		ProcessSchedule.pcb_all.remove(this);
		OSmain.Log("进程 " + ProcessName + " (ProcessID="+this.ProcessID+") 消亡");
		status = 6;
	}


	public void caculateLeftTime() {
		Instruction CI = CurrentInstruction;
		TimeRemained = 0;
		while (CI.next != null) {
			TimeRemained += CI.getRunTime();
			CI = CI.next;
		}
	}


}
