package com.yangheng.OS.PCBUtil;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PRCView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public PRCView() {
		setTitle("PRC\u9884\u89C8");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.append(GenRamPCB.PRC);
		scrollPane.setViewportView(textArea);

	}
}
