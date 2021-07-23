package com.yangheng.OS.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.yangheng.OS.Main.OSmain;
import com.yangheng.OS.Main.ProcessSchedule;
import com.yangheng.OS.Model.Instruction;
import com.yangheng.OS.Model.PCB;
import com.yangheng.OS.PCBUtil.ProcessTraceFrm;

public class FileUtil {
	public OSmain osmain;

	public FileUtil(OSmain osmain) {
		this.osmain = osmain;
	}

	public static void Log(String log) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
		long time = date.getTime();
		String time1 = simpleDateFormat.format(time);
		File file = new File("C:\\Users\\chuan\\Desktop\\Logs\\log" + time1 + ".log");
		try {
			file.createNewFile(); // 创建文件
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] bt = OSmain.textArea.getText().getBytes();
		try {
			FileOutputStream in = new FileOutputStream(file);
			try {
				in.write(bt, 0, bt.length);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从filepath读去各种格式的PRC，然后将解析出PCB队列，并队主窗体osmain进行更新
	 * 
	 * @param filepath
	 *            文件路径
	 * @param osmain
	 *            主窗体
	 */
	public void ReadPRC(String filepath, OSmain osmain) {// 现在已支持各种形式的PRC书写格式
		ProcessSchedule.pcb_all = new ArrayList<>();
		ProcessSchedule.pcb_ready = new ArrayList<>();
		ProcessSchedule.pcb_backupready = new ArrayList<>();
		ProcessSchedule.pcb_input = new ArrayList<>();
		ProcessSchedule.pcb_output = new ArrayList<>();
		ProcessSchedule.pcb_wait = new ArrayList<>();
		String prc = "";
		try {
			File file = new File(filepath);
			BufferedReader reader = null;
			reader = new BufferedReader(new FileReader(file));
			String tempString = "";
			int PCBID = 1;
			while ((tempString = reader.readLine()) != null) {
				try {
					Integer.parseInt(tempString);
				} catch (Exception e) {
					prc += tempString + " ";
				}
			}
			String[] PCBInfo = prc.replaceAll("  ", " ").toUpperCase().split(" ");
			PCB tempPCB = null;
			for (int i = 0; i < PCBInfo.length;) {
				boolean b = false;
				while (PCBInfo[i].charAt(0) == 'P') {
					tempPCB = new PCB(PCBID++, PCBInfo[i]);
					i++;
					while (PCBInfo[i].charAt(0) != 'P' && !b) {
						Instruction instruction = new Instruction(String.valueOf(PCBInfo[i].charAt(0)),
								Integer.valueOf(PCBInfo[i].substring(1, PCBInfo[i].length())));
						tempPCB.addInstruction(instruction);
						if (i < PCBInfo.length - 1) {
							i++;
						} else {
							b = true;
							break;
						}
					}
					ProcessSchedule.addPCB_all(tempPCB);
					ProcessSchedule.addPCB_backupready(tempPCB);
				}
				if (b)
					break;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Vector<String> vector = new Vector<>();
		for (int i = 0; i < ProcessSchedule.pcb_backupready.size(); i++) {
			vector.add(ProcessSchedule.pcb_backupready.get(i).ProcessName);
		}
		OSmain.list2 = new JList<>();
		OSmain.list2.setListData(vector);
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
		OSmain.list2.setListData(vector);
		osmain.frame.getContentPane().remove(osmain.scrollPane2);
		osmain.scrollPane2 = new JScrollPane(OSmain.list2);
		osmain.scrollPane2.setBounds(224, 426, 145, 228);
		osmain.frame.getContentPane().add(osmain.scrollPane2);
		osmain.scrollPane2.validate();
	}

}
