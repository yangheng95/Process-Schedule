package com.yangheng.OS.PCBUtil;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.yangheng.OS.Main.ProcessSchedule;
import com.yangheng.OS.Model.Instruction;
import com.yangheng.OS.Model.PCB;

import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.JTextField;

public class ProcessTraceFrm extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Pname;
	private JTextField PID;
	private JTextField PStatus;
	private JTextField PFormer;
	private JTextField PNext;
	private JTextField Pstart;
	private JTextField PInstructions;
	PCB pcb;
	String pname;


	public ProcessTraceFrm(String pname) {
		this.pname = pname;
		try {
			UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(-3, 34, 400, 320);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("\u8FDB\u7A0B\u540D\u79F0");
		lblNewLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		lblNewLabel.setBounds(22, 27, 73, 26);
		contentPane.add(lblNewLabel);

		JLabel lblid = new JLabel("\u8FDB\u7A0BID");
		lblid.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		lblid.setBounds(22, 63, 73, 26);
		contentPane.add(lblid);

		JLabel label_1 = new JLabel("\u8FDB\u7A0B\u524D\u9A71");
		label_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		label_1.setBounds(22, 134, 73, 26);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("\u8FDB\u7A0B\u540E\u7EE7");
		label_2.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		label_2.setBounds(22, 170, 73, 26);
		contentPane.add(label_2);

		JLabel label_3 = new JLabel("\u5F00\u59CB\u65F6\u95F4");
		label_3.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		label_3.setBounds(22, 206, 73, 26);
		contentPane.add(label_3);

		JLabel label_4 = new JLabel("\u5269\u4F59\u6307\u4EE4\u96C6");
		label_4.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		label_4.setBounds(22, 242, 73, 26);
		contentPane.add(label_4);

		JLabel label = new JLabel("\u8FDB\u7A0B\u72B6\u6001");
		label.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		label.setBounds(22, 99, 73, 26);
		contentPane.add(label);

		Pname = new JTextField();
		Pname.setBounds(105, 30, 246, 21);
		contentPane.add(Pname);
		Pname.setColumns(10);

		PID = new JTextField();
		PID.setColumns(10);
		PID.setBounds(105, 66, 246, 21);
		contentPane.add(PID);

		PStatus = new JTextField();
		PStatus.setColumns(10);
		PStatus.setBounds(105, 102, 246, 21);
		contentPane.add(PStatus);

		PFormer = new JTextField();
		PFormer.setColumns(10);
		PFormer.setBounds(105, 137, 246, 21);
		contentPane.add(PFormer);

		PNext = new JTextField();
		PNext.setColumns(10);
		PNext.setBounds(105, 173, 246, 21);
		contentPane.add(PNext);

		Pstart = new JTextField();
		Pstart.setColumns(10);
		Pstart.setBounds(105, 209, 246, 21);
		contentPane.add(Pstart);

		PInstructions = new JTextField();
		PInstructions.setColumns(10);
		PInstructions.setBounds(105, 245, 246, 21);
		contentPane.add(PInstructions);
	}

	@Override
	public void run() {
		this.setTitle(pname+"ÏêÇé"+"(ÊµÊ±¸üÐÂ)");
		while (true) {
			
			for (int i = 0; i < ProcessSchedule.pcb_all.size(); i++) {
				if (pname.equals(ProcessSchedule.pcb_all.get(i).ProcessName))
					
					pcb = ProcessSchedule.pcb_all.get(i);
			}
			this.Pname.setText(pcb.ProcessName);
			this.PStatus.setText(pcb.CurrentInstruction.getInstructionName());
			this.PID.setText(String.valueOf(pcb.ProcessID));
			if (pcb.getFormer() != null)
				this.PFormer.setText(pcb.getFormer().ProcessName);
			if (pcb.getNext() != null)
				this.PNext.setText(pcb.getNext().ProcessName);
			String Instructions = "";
			Instruction Itemp = pcb.CurrentInstruction;
			while (Itemp.next != null) {
				Instructions += Itemp.getInstructionName() + " ";
				Itemp = Itemp.next;
			}
			this.PInstructions.setText(Instructions);
			this.Pstart.setText(pcb.StartTime);
			if(pcb.CurrentInstruction.getInstructionName().equals("H"))
				this.dispose();
			try {
				Thread.sleep(ProcessSchedule.timeship/2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
