package com.yangheng.OS.PCBUtil;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.yangheng.OS.Main.OSmain;
import com.yangheng.OS.Main.ProcessSchedule;
import com.yangheng.OS.Model.Instruction;
import com.yangheng.OS.Model.PCB;

public class GenRamPCB {// Ëæ»úÉú³ÉPCB
	public static String PRC = "";

	public static PCB generateRamPCB() {
		PCB pcb = new PCB(OSmain.MaxPID, "RamProcess" + OSmain.MaxPID);

		PRC += "RamProcess" + OSmain.MaxPID + " ";
		int InstructionNum = 1 + (int) (Math.random() * 9);
		OSmain.MaxPID++;
		PRC += "C" + (int) (Math.random() * 10000) + " ";
		Instruction instruction = new Instruction("C", (int) (Math.random() * 10000));
		pcb.addInstruction(instruction);
		for (int i = 0; i < InstructionNum - 1; i++) {
			String ins = "";
			int time = 0;
			if (instruction.getInstructionName().equals("C")) {
				switch ((int) (Math.random() * 3)) {
				case 0:
					ins += "I";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "I" + (int) (Math.random() * 10000) + " ";
					break;
				case 1:
					ins += "O";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "O" + (int) (Math.random() * 10000) + " ";
					break;
				case 2:
					ins += "W";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "W" + (int) (Math.random() * 10000) + " ";
					break;

				default:
					break;
				}

			} else if (instruction.getInstructionName().equals("I")) {
				switch ((int) (Math.random() * 3)) {

				case 0:
					ins += "C";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "C" + (int) (Math.random() * 10000) + " ";
					break;
				case 1:
					ins += "O";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "O" + (int) (Math.random() * 10000) + " ";
					break;
				case 2:
					ins += "W";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "W" + (int) (Math.random() * 10000) + " ";
					break;

				default:
					break;
				}
			} else if (instruction.getInstructionName().equals("O")) {
				switch ((int) (Math.random() * 3)) {

				case 0:
					ins += "C";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "C" + (int) (Math.random() * 10000) + " ";
					break;
				case 1:
					ins += "I";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "I" + (int) (Math.random() * 10000) + " ";
					break;
				case 2:
					ins += "W";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "W" + (int) (Math.random() * 10000) + " ";
					break;
				default:
					break;
				}
			} else if (instruction.getInstructionName().equals("W")) {
				switch ((int) (Math.random() * 3)) {

				case 0:
					ins += "C";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "C" + (int) (Math.random() * 10000) + " ";
					break;
				case 1:
					ins += "I";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "I" + (int) (Math.random() * 10000) + " ";
					break;
				case 2:
					ins += "O";
					time = (int) (Math.random() * 10000);
					instruction = new Instruction(ins, time);
					pcb.addInstruction(instruction);
					PRC += "O" + (int) (Math.random() * 10000) + " ";
					break;

				default:
					break;
				}
			}
		}
		pcb.addInstruction(new Instruction("H", 0));
		PRC += "H" + 0 + "\n";
		return pcb;
	}

	public static String generatePRCQue(int PCBnum, OSmain osmain) {
		PRC = "";
		ProcessSchedule.pcb_all = new ArrayList<>();
		ProcessSchedule.pcb_ready = new ArrayList<>();
		ProcessSchedule.pcb_backupready = new ArrayList<>();
		ProcessSchedule.pcb_input = new ArrayList<>();
		ProcessSchedule.pcb_output = new ArrayList<>();
		ProcessSchedule.pcb_wait = new ArrayList<>();
		Vector<String> vector = new Vector<>();
		for (int i = 0; i < PCBnum; i++) {
			PCB pcb = generateRamPCB();
			vector.addElement(pcb.ProcessName);
			ProcessSchedule.addPCB_all(pcb);
			ProcessSchedule.addPCB_backupready(pcb);
		}
		Vector<String> vector1 = new Vector<>();
		for(int i = 0;i < ProcessSchedule.pcb_backupready.size();i++){
			vector1.add(ProcessSchedule.pcb_backupready.get(i).ProcessName);
		}
		OSmain.list2 = new JList<>();
		OSmain.list2.setListData(vector1);
		OSmain.list2.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("deprecation")
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == true) {
					ProcessTraceFrm processTraceFrm = new ProcessTraceFrm(OSmain.list2.getSelectedValue());
					processTraceFrm.show();
					new Thread(processTraceFrm).start();
					// processTraceFrm.setVisible(true);}
				}
			}
		});
		OSmain.list2.setListData(vector1);
		osmain.frame.getContentPane().remove(osmain.scrollPane2);
		osmain.scrollPane2 = new JScrollPane(OSmain.list2);
		osmain.scrollPane2.setBounds(224, 426, 145, 228);
		osmain.frame.getContentPane().add(osmain.scrollPane2);
		osmain.scrollPane2.validate();

		return PRC;
	}


}
