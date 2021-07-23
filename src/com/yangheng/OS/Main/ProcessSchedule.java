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
	 * ��ʼ�������࣬���������ڱ���ʹ���������ʵʱ���������ڿؼ���ʾ��Ϣ
	 * @param timeship	ʱ��Ƭ��С
	 * @param osmain	������ʵ������
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
		OSmain.Log("--ģ����̵��ȿ�ʼ--");
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
	 * ͨ������������ʵ�������ַ����������ؼ�
	 */
	@SuppressWarnings("deprecation")
	public void updateMainUI() {
		v1 = new Vector<>();
		v2 = new Vector<>();
		v3 = new Vector<>();
		v4 = new Vector<>();
		v5 = new Vector<>();

		// String log = "\r\n���ж���:";
		// for (int i = 0; i < pcb_all.size(); i++) {
		// log += pcb_all.get(i).ProcessName + "(" + pcb_all.get(i).status + ")
		// ";
		// }
		// log += "\r\n�󱸶���:";

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
		{// ����Main������Ϣ
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
		} // ����Main������Ϣ
		if (pcb_all.isEmpty() && !osmain.isOver) {
			OSmain.Log("�������л��ߺ󱸶��ж���û�н��̣�������ϣ�");
			osmain.isOver = true;
			FileUtil.Log(OSmain.textArea.getText());
			JOptionPane.showMessageDialog(null, "�������л��ߺ󱸶��ж���û�н��̣�������ϣ�");
			osmain.menuItem_2.setEnabled(true);
			OSmain.Log("--ģ����Ƚ���--");
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
				OSmain.Log("���߳�" + pcb_backupready.get(pcb_backupready.size() - 1).ProcessName + "����");
			}
		}
		if (!updateAllPCBStatus()) {
			if (pcb_backupready.size() > 0 && pcb_ready.size() < 10) {
				ScheduleFromBackupReady();
			} else if (pcb_ready.size() > 0) {
				ScheduleFromReady();
			} else {
				OSmain.Log("��û�н��̴��ھ������У��󱸶���Ҳû����ҵ�ɹ�����");
			}
		}

	}

	public boolean ScheduleFromBackupReady() {// ���Ƚ���ǿռCPU
		if (CurrentRunPCB == null) {// ����������������ǰ����Ϊ��
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
	} // �������

	public boolean ScheduleFromReady() {// ���Ƚ���ǿռCPU
		if (CurrentRunPCB == null) {// ����������������ǰ����Ϊ��
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
			// ��ȥָ�������е�ʱ��Ƭ 
			if (pcurrent.status == 0 || pcurrent.status == 3 || pcurrent.status == 4 || pcurrent.status == 5) {
				if (pcurrent.CurrentInstruction.getRunTime() <= 0 && pcurrent.CurrentInstruction.next != null) {
					// ���ָ���ѱ�ִ���꣬��ת����һ��ָ��
					if (pcurrent.CurrentInstruction.getInstructionName().equals("I")) {
						OSmain.Log(pcurrent.ProcessName + "(ProcessID=" + pcurrent.ProcessID + ") �������");
					} else if (pcurrent.CurrentInstruction.getInstructionName().equals("O")) {
						OSmain.Log(pcurrent.ProcessName + "(ProcessID=" + pcurrent.ProcessID + ") ������");
					} else if (pcurrent.CurrentInstruction.getInstructionName().equals("W")) {
						OSmain.Log(pcurrent.ProcessName + "(ProcessID=" + pcurrent.ProcessID + ") �ȴ����");
					}
					pcurrent.CurrentInstruction = pcurrent.CurrentInstruction.next;
					// System.out.println(pcurrent.ProcessName+pcurrent.CurrentInstruction.toString());
					if (pcurrent.CurrentInstruction.getInstructionName().equals("I")) {
						// �����һ��ָ��������ָ��
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
						pcurrent.getNext().setFormer(null);// �ӽ��̵�ǰ�����Ƴ���ǰPCB,��������ͷ����
					}
				} else if (pcurrent.CurrentInstruction.getRunTime() > 0) {
					if (pcurrent.status == 0) {
						pcurrent.status = 1;
						if (ProcessSchedule.pcb_ready.isEmpty()) {
							pcurrent.setFormer(null);
							pcurrent.setNext(null); // ��һ�ν���
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
						OSmain.Log("����" + pcurrent.ProcessName + "��ͣ���У������������");
					}
				}
			}
		}
		return b;
	}
}
