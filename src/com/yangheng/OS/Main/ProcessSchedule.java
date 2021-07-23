package com.yangheng.OS.Main;

import com.yangheng.OS.Log.FileUtil;
import com.yangheng.OS.Model.Instruction;
import com.yangheng.OS.Model.PCB;
import com.yangheng.OS.PCBUtil.GenRamPCB;
import com.yangheng.OS.PCBUtil.ProcessTraceFrm;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ProcessSchedule extends Thread {

	public static PCB CurrentRunPCB = null;
	public static ArrayList<PCB> pcb_all = new ArrayList<>();
	public static ArrayList<PCB> pcb_wait = new ArrayList<>();
	public static ArrayList<PCB> pcb_input = new ArrayList<>();
	public static ArrayList<PCB> pcb_output = new ArrayList<>();
	public static ArrayList<PCB> pcb_ready = new ArrayList<>();
	public static ArrayList<PCB> pcb_backupready = new ArrayList<>();

	public static int timeship = 0;
	public static long start = System.currentTimeMillis();
	int maxProcess = 3;
	OSmain osmain;
	public static int realruntime = 0; 
	
	Vector<String> v1, v2, v3, v4, v5;

	/**
	 * 初始化调度类，传入主窗口变量使调度类可以实时更新主窗口控件显示信息
	 * @param timeship	时间片大小
	 * @param osmain	主界面实例对象
	 */
	public ProcessSchedule(int timeship, OSmain osmain) {
		ProcessSchedule.timeship = timeship;
		this.osmain = osmain;
	}


	public static void addPCB_all(PCB pcb) {
		pcb_all.add(pcb);
	}

	public static void addPCB_input(PCB pcb) {
		for (int i = 0; i < pcb_input.size(); i++) {
			if (pcb_input.get(i).getNext() == null) {
				pcb_input.get(i).setNext(pcb);
				break;
			}
		}
		pcb_input.add(pcb);
	}

	public static void addPCB_output(PCB pcb) {
		for (int i = 0; i < pcb_output.size(); i++) {
			if (pcb_output.get(i).getNext() == null) {
				pcb_output.get(i).setNext(pcb);
				break;
			}
		}
		pcb_output.add(pcb);
	}

	public static void addPCB_ready(PCB pcb) {
		for (int i = 0; i < pcb_ready.size(); i++) {
			if (pcb_ready.get(i).getNext() == null) {
				pcb_ready.get(i).setNext(pcb);
				break;
			}
		}
		pcb_ready.add(pcb);
	}

	public static void addPCB_backupready(PCB pcb) {
		for (int i = 0; i < pcb_backupready.size(); i++) {
			if (pcb_backupready.get(i).getNext() == null) {
				pcb_backupready.get(i).setNext(pcb);
				break;
			}
		}
		pcb_backupready.add(pcb);
	}

	public void addPCB_wait(PCB pcb) {
		for (int i = 0; i < pcb_wait.size(); i++) {
			if (pcb_wait.get(i).getNext() == null) {
				pcb_wait.get(i).setNext(pcb);
				break;
			}
		}
		pcb_wait.add(pcb);
	}


	@Override
	public void run() {
		OSmain.Log("--模拟进程调度开始--");
		start = System.currentTimeMillis();
		while (true) {
			timeship = Integer.parseInt(osmain.textField.getText());
			while ((int) (System.currentTimeMillis() - start) >= timeship) {
				while (osmain.onPause) {
					try {
						sleep(timeship/2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				start = System.currentTimeMillis();
				Schedule();
				updateMainUI();
			}
		}
	}

	/**
	 * 通过引用主界面实例对象地址更新主界面控件
	 */
	@SuppressWarnings("deprecation")
	public void updateMainUI() {
		v1 = new Vector<>();
		v2 = new Vector<>();
		v3 = new Vector<>();
		v4 = new Vector<>();
		v5 = new Vector<>();

		// String log = "\r\n所有队列:";
		// for (int i = 0; i < pcb_all.size(); i++) {
		// log += pcb_all.get(i).ProcessName + "(" + pcb_all.get(i).status + ")
		// ";
		// }
		// log += "\r\n后备队列:";

		String string = "";
		for (int i = 0; i < pcb_backupready.size(); i++) {
			v2.addElement(pcb_backupready.get(i).ProcessName);
			string += pcb_backupready.get(i).ProcessName + " ";
		}
		osmain.textField_1.setText(string);
		for (int i = 0; i < pcb_ready.size(); i++) {
			v1.addElement(pcb_ready.get(i).ProcessName);
		}
		for (int i = 0; i < pcb_input.size(); i++) {
			v3.addElement(pcb_input.get(i).ProcessName);
		}
		for (int i = 0; i < pcb_output.size(); i++) {
			v4.addElement(pcb_output.get(i).ProcessName);
		}
		for (int i = 0; i < pcb_wait.size(); i++) {
			v5.addElement(pcb_wait.get(i).ProcessName);
		}

		OSmain.list1 = new JList<>();
		OSmain.list2 = new JList<>();
		OSmain.list3 = new JList<>();
		OSmain.list4 = new JList<>();
		OSmain.list5 = new JList<>();

		OSmain.list1.setListData(v1);
		osmain.frame.getContentPane().remove(osmain.scrollPane1);
		osmain.scrollPane1 = new JScrollPane(OSmain.list1);
		osmain.scrollPane1.setBounds(34, 426, 145, 228);
		osmain.frame.getContentPane().add(osmain.scrollPane1);

		OSmain.list2.setListData(v2);
		osmain.frame.getContentPane().remove(osmain.scrollPane2);
		osmain.scrollPane2 = new JScrollPane(OSmain.list2);
		osmain.scrollPane2.setBounds(224, 426, 145, 228);
		osmain.frame.getContentPane().add(osmain.scrollPane2);

		OSmain.list3.setListData(v3);
		osmain.frame.getContentPane().remove(osmain.scrollPane3);
		osmain.scrollPane3 = new JScrollPane(OSmain.list3);
		osmain.scrollPane3.setBounds(411, 426, 145, 228);
		osmain.frame.getContentPane().add(osmain.scrollPane3);

		OSmain.list4.setListData(v4);
		osmain.frame.getContentPane().remove(osmain.scrollPane4);
		osmain.scrollPane4 = new JScrollPane(OSmain.list4);
		osmain.scrollPane4.setBounds(605, 426, 145, 228);
		osmain.frame.getContentPane().add(osmain.scrollPane4);

		OSmain.list5.setListData(v5);
		osmain.frame.getContentPane().remove(osmain.scrollPane5);
		osmain.scrollPane5 = new JScrollPane(OSmain.list5);
		osmain.scrollPane5.setBounds(796, 426, 145, 228);
		osmain.frame.getContentPane().add(osmain.scrollPane5);

		OSmain.list1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == true) {
					ProcessTraceFrm processTraceFrm = new ProcessTraceFrm(OSmain.list1.getSelectedValue());
					processTraceFrm.show();
					new Thread(processTraceFrm).start();
				}
			}
		});
		OSmain.list2.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == true) {
					ProcessTraceFrm processTraceFrm = new ProcessTraceFrm(OSmain.list2.getSelectedValue());
					processTraceFrm.show();
					new Thread(processTraceFrm).start();
				}
			}
		});
		OSmain.list3.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == true) {
					ProcessTraceFrm processTraceFrm = new ProcessTraceFrm(OSmain.list3.getSelectedValue());
					processTraceFrm.show();
					new Thread(processTraceFrm).start();
				}
			}
		});
		OSmain.list4.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == true) {
					ProcessTraceFrm processTraceFrm = new ProcessTraceFrm(OSmain.list4.getSelectedValue());
					processTraceFrm.show();
					new Thread(processTraceFrm).start();
				}
			}
		});
		OSmain.list5.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == true) {
					ProcessTraceFrm processTraceFrm = new ProcessTraceFrm(OSmain.list5.getSelectedValue());
					processTraceFrm.show();
					new Thread(processTraceFrm).start();
				}
			}
		});
		{// 设置Main窗口信息
			if (CurrentRunPCB != null) {
				osmain.CurrentProcess.setText(CurrentRunPCB.ProcessName);
				// osmain.haveRuned = new JLabel("");
				osmain.nextInstruction.setText(CurrentRunPCB.CurrentInstruction.next.getInstructionName());
				String Instructions = "";
				osmain.haveRuned.setText(CurrentRunPCB.TimeRemained + "ms");
				Instruction Itemp = CurrentRunPCB.CurrentInstruction;
				while (Itemp.next != null) {
					Instructions += Itemp.getInstructionName() + " ";
					Itemp = Itemp.next;
				}
				osmain.InstructionQue.setText(Instructions);
				if(CurrentRunPCB.getNext()!=null){
					osmain.nextProcess.setText(CurrentRunPCB.getNext().ProcessName);
				}
				else {
					osmain.CurrentProcess.setText("CPU avaliable");
					}
			} else {
				osmain.CurrentProcess.setText("CPU avaliable");
				osmain.haveRuned.setText("");
				osmain.nextInstruction.setText("");
				osmain.InstructionQue.setText("");
				osmain.nextProcess.setText("");
			}
		} // 设置Main窗口信息
		if (pcb_all.isEmpty() && !osmain.isOver) {
			OSmain.Log("就绪队列或者后备队列都已没有进程，调度完毕！");
			osmain.isOver = true;
			FileUtil.Log(OSmain.textArea.getText());
			JOptionPane.showMessageDialog(null, "就绪队列或者后备队列都已没有进程，调度完毕！");
			osmain.menuItem_2.setEnabled(true);
			OSmain.Log("--模拟调度结束--");
			stop();
		}
		osmain.frame.getContentPane().validate();
	}

	public void Schedule() {
		if (OSmain.radiobtn.isSelected()) {
			int P = (int) (Math.random() * 100);
			if (P < 5) {
				PCB pcb = GenRamPCB.generateRamPCB();
				addPCB_backupready(pcb);
				addPCB_all(pcb);
				OSmain.Log("新线程" + pcb_backupready.get(pcb_backupready.size() - 1).ProcessName + "加入");
			}
		}
		if (!updateAllPCBStatus()) {
			if (pcb_backupready.size() > 0 && pcb_ready.size() < 10) {
				ScheduleFromBackupReady();
			} else if (pcb_ready.size() > 0) {
				ScheduleFromReady();
			} else {
				OSmain.Log("已没有进程处于就绪队列，后备队列也没有作业可供调度");
			}
		}

	}

	public boolean ScheduleFromBackupReady() {// 调度进程强占CPU
		if (CurrentRunPCB == null) {// 进程生命结束，当前进程为空
			if (!pcb_backupready.isEmpty()) {
				for (int i = 0; i < pcb_backupready.size(); i++) {
					if (pcb_backupready.get(i).getFormer() == null) {
						CurrentRunPCB = pcb_backupready.get(i);
						pcb_backupready.get(i).RunOnCpu();
						return true;
					}
				}
			} else {
				return false;
			}
		} else {
			for (int i = 0; i < pcb_backupready.size(); i++) {
				if (pcb_backupready.get(i).getFormer() == null) {
					CurrentRunPCB = pcb_backupready.get(i);
					pcb_backupready.get(i).RunOnCpu();
					return true;
				}
			}
			return false;
		}
		return false;
	} // 调度完毕

	public boolean ScheduleFromReady() {// 调度进程强占CPU
		if (CurrentRunPCB == null) {// 进程生命结束，当前进程为空
			if (!pcb_ready.isEmpty()) {
				for (int i = 0; i < pcb_ready.size(); i++) {
					if (pcb_ready.get(i).getFormer() == null) {
						CurrentRunPCB = pcb_ready.get(i);
						pcb_ready.get(i).RunOnCpu();
						return true;
					}
				}
			} else {
				return false;
			}
		} else {
			for (int i = 0; i < pcb_ready.size(); i++) {
				if (pcb_ready.get(i).getFormer() == null) {
					CurrentRunPCB = pcb_ready.get(i);
					pcb_ready.get(i).RunOnCpu();
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public boolean updateAllPCBStatus() {
		boolean b = false;
		for (int i = 0; i < ProcessSchedule.pcb_all.size(); i++) {
			PCB pcurrent = ProcessSchedule.pcb_all.get(i);
			pcurrent.caculateLeftTime();
			if (!pcurrent.CurrentInstruction.getInstructionName().equals("C")) {
				pcurrent.CurrentInstruction
						.setRunTime(pcurrent.CurrentInstruction.getRunTime() - realruntime);
			}
			// 减去指令已运行的时间片 
			if (pcurrent.status == 0 || pcurrent.status == 3 || pcurrent.status == 4 || pcurrent.status == 5) {
				if (pcurrent.CurrentInstruction.getRunTime() <= 0 && pcurrent.CurrentInstruction.next != null) {
					// 如果指令已被执行完，跳转到下一条指令
					if (pcurrent.CurrentInstruction.getInstructionName().equals("I")) {
						OSmain.Log(pcurrent.ProcessName + "(ProcessID=" + pcurrent.ProcessID + ") 输入完成");
					} else if (pcurrent.CurrentInstruction.getInstructionName().equals("O")) {
						OSmain.Log(pcurrent.ProcessName + "(ProcessID=" + pcurrent.ProcessID + ") 输出完成");
					} else if (pcurrent.CurrentInstruction.getInstructionName().equals("W")) {
						OSmain.Log(pcurrent.ProcessName + "(ProcessID=" + pcurrent.ProcessID + ") 等待完成");
					}
					pcurrent.CurrentInstruction = pcurrent.CurrentInstruction.next;
					// System.out.println(pcurrent.ProcessName+pcurrent.CurrentInstruction.toString());
					if (pcurrent.CurrentInstruction.getInstructionName().equals("I")) {
						// 如果下一条指令是输入指令
						pcurrent.waitForInput();
					} else if (pcurrent.CurrentInstruction.getInstructionName().equals("C") && pcurrent.status != 0
							&& pcurrent.status != 1 && pcurrent.status != 2) {
						pcurrent.RunOnCpu();
						b = true;
					} else if (pcurrent.CurrentInstruction.getInstructionName().equals("O")) {
						pcurrent.waitForOutput();
					} else if (pcurrent.CurrentInstruction.getInstructionName().equals("W")) {
						pcurrent.Wait();
					}
					if (pcurrent.CurrentInstruction.next == null
							|| pcurrent.CurrentInstruction.getInstructionName().equals("H")) {
						pcurrent.remove();
					}
					if (pcurrent.getNext() != null) {
						pcurrent.getNext().setFormer(null);// 从进程当前队列移除当前PCB,并将队列头更新
					}
				} else if (pcurrent.CurrentInstruction.getRunTime() > 0) {
					if (pcurrent.status == 0) {
						pcurrent.status = 1;
						if (ProcessSchedule.pcb_ready.isEmpty()) {
							pcurrent.setFormer(null);
							pcurrent.setNext(null); // 第一次进入
							ProcessSchedule.pcb_ready.add(pcurrent);
						} else {
							for (int i1 = 0; i1 < ProcessSchedule.pcb_ready.size(); i1++) {
								if (ProcessSchedule.pcb_ready.get(i1).getNext() == null) {
									ProcessSchedule.pcb_ready.get(i1).setNext(pcurrent);
									pcurrent.setFormer(ProcessSchedule.pcb_ready.get(i1));
									ProcessSchedule.pcb_ready.add(pcurrent);
									pcurrent.setNext(null);
									break;
								}
							}
						}
						OSmain.Log("进程" + pcurrent.ProcessName + "暂停运行，进入就绪队列");
					}
				}
			}
		}
		return b;
	}
}
